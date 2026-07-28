package in.tech_camp.prototype_d.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.tech_camp.prototype_d.custom_user.CustomUserDetail;
import in.tech_camp.prototype_d.dto.PrototypeDto;
import in.tech_camp.prototype_d.service.PrototypeService;
import lombok.RequiredArgsConstructor;
import in.tech_camp.prototype_d.repository.PrototypeRepository;
import in.tech_camp.prototype_d.entity.PrototypeEntity;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import in.tech_camp.prototype_d.entity.UserEntity;
import in.tech_camp.prototype_d.form.CommentForm;
import in.tech_camp.prototype_d.form.PrototypeForm;
import in.tech_camp.prototype_d.form.SearchForm;
import in.tech_camp.prototype_d.repository.UserRepository;
import in.tech_camp.prototype_d.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/prototypes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PrototypeController {

    private final PrototypeService prototypeService;
    private final PrototypeRepository prototypeRepository;

    /**
     * プロトタイプ一覧取得
     * GET: /api/prototypes
     */
    @GetMapping
    public ResponseEntity<?> getPrototypes() {
        try {
            List<PrototypeDto> prototypes = prototypeService.getPrototypes();
            return ResponseEntity.ok().body(prototypes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("messages", List.of("投稿の取得に失敗しました。")));
        }
    }

    /**
     * プロトタイプ新規作成 (画像アップロード含む)
     * POST: /api/prototypes
     */
    @PostMapping
    public ResponseEntity<?> createPrototype(
        @Validated @ModelAttribute PrototypeForm prototypeForm,
        BindingResult bindingResult,
        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
        @AuthenticationPrincipal CustomUserDetail customUserDetails // ★ 1. ログインユーザー情報を取得
    ) {
        // 未ログイン時のチェック (必要に応じて)
        if (customUserDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("messages", List.of("ログインが必要です")));
        }

        // 1. フォームの基本入力チェック
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(Map.of("messages", errorMessages));
        }

        // 2. 画像ファイルの必須チェック
        if (imageFile == null || imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("messages", List.of("画像ファイルを選択してください")));
        }

        try {
            // 3. 画像の保存処理 (uploads/ フォルダへ保存)
            String uploadDir = Paths.get(System.getProperty("user.dir"), "uploads").toAbsolutePath().toString();
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String originalFilename = imageFile.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + extension;

            File dest = new File(dir, fileName);
            imageFile.transferTo(dest);

            // 4. Entityへのセット
            PrototypeEntity entity = new PrototypeEntity();
            entity.setTitle(prototypeForm.getTitle());
            entity.setCatchcopy(prototypeForm.getCatchcopy());
            entity.setConcept(prototypeForm.getConcept());
            entity.setImage(fileName); // ファイル名をセット

            // ★ 2. ダミーユーザーではなく、認証済みのログインユーザーをセット
            // customUserDetails から UserEntity を取得（メソッド名は CustomUserDetail の実装に合わせてください）
            UserEntity currentUser = customUserDetails.getUser(); 
            entity.setUser(currentUser);

            // 5. DB保存（Service経由で保存）
            prototypeService.insert(entity);

            return ResponseEntity.ok().body(entity);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("messages", List.of("画像の保存に失敗しました")));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("messages", List.of("データベースへの保存に失敗しました")));
        }
    }

    // プロトタイプ詳細表示
    @GetMapping("/{prototypeId}")
    public ResponseEntity<?> showPrototypeDetail(@PathVariable("prototypeId") Long prototypeId) {
        try {
            PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
            if(prototype == null){
                return ResponseEntity.notFound().build(); 
            }
            return ResponseEntity.ok().body(prototype);
        } catch (Exception e) {
            System.out.println("エラー :" + e);
            return ResponseEntity.internalServerError().body("サーバーエラー");
        }
    }

    // プロトタイプ削除機能
    @DeleteMapping("/{prototypeId}")
    public ResponseEntity<?> deletePrototype(@PathVariable("prototypeId") Long prototypeId, 
                                              @AuthenticationPrincipal CustomUserDetail currentUser) {
        try {
            // currentUser.getId() も Long を返す必要があります
            prototypeService.deletePrototype(prototypeId, currentUser.getId());
            return ResponseEntity.ok().body(Map.of(
                "Message", "プロトタイプを削除しました", "deletePrototypeId", prototypeId
            ));
        } catch (IllegalArgumentException e) {
            // 削除が許可されてないユーザーをはじく処理
            return ResponseEntity.status(403).body(Map.of("messages", List.of(e.getMessage())));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("messages", List.of("プロトタイプの削除に失敗しました")));
        }
    }
}
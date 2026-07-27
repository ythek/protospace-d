package in.tech_camp.prototype_d.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.prototype_d.custom_user.CustomUserDetail;
import in.tech_camp.prototype_d.dto.PrototypeDto;
import in.tech_camp.prototype_d.entity.PrototypeEntity;
import in.tech_camp.prototype_d.entity.UserEntity;
import in.tech_camp.prototype_d.form.CommentForm;
import in.tech_camp.prototype_d.form.PrototypeForm;
import in.tech_camp.prototype_d.form.SearchForm;
import in.tech_camp.prototype_d.repository.PrototypeRepository;
import in.tech_camp.prototype_d.repository.UserRepository;
import in.tech_camp.prototype_d.service.PrototypeService;
import in.tech_camp.prototype_d.validation.ValidationOrder;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

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
     * プロトタイプ詳細取得
     * GET: /api/prototypes/{prototypeId}
     */
    @GetMapping("/{prototypeId}")
    public ResponseEntity<?> showPrototypeDetail(@PathVariable("prototypeId") Integer prototypeId) {
        try {
            PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
            if (prototype == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().body(prototype);
        } catch (Exception e) {
            System.out.println("エラー: " + e);
            return ResponseEntity.internalServerError().body(Map.of("messages", List.of("サーバーエラーが発生しました。")));
        }
    }

    /**
     * プロトタイプ新規作成 (画像アップロード含む)
     * POST: /api/prototypes
     */
    // @PostMapping
    // public ResponseEntity<?> createPrototype(
    //         @ModelAttribute @Validated(ValidationOrder.class) PrototypeForm prototypeForm,
    //         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
    //         BindingResult result
    //         // @AuthenticationPrincipal 
    //         // CustomUserDetail currentUser
    //         ) {
    // @PostMapping
    // public ResponseEntity<?> createPrototype(
    //         @ModelAttribute @Validated(ValidationOrder.class) PrototypeForm prototypeForm,
    //         BindingResult result, // ⭕ @Validated(prototypeForm) のすぐ直後に配置！
    //         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    //         // @AuthenticationPrincipal 
    //         // CustomUserDetail currentUser
    //         ) {

    //     // 1. バリデーションエラー判定
    //     if (result.hasErrors()) {
    //         List<String> errorMessages = result.getAllErrors().stream()
    //                 .map(DefaultMessageSourceResolvable::getDefaultMessage)
    //                 .collect(Collectors.toList());
    //         return ResponseEntity.badRequest().body(Map.of("messages", errorMessages));
    //     }

    //     // 2. 画像ファイルの存在チェック
    //     if (imageFile == null || imageFile.isEmpty()) {
    //         return ResponseEntity.badRequest().body(Map.of("messages", List.of("画像ファイルを選択してください")));
    //     }

    //     // 3. 画像の保存処理 (uploads/ フォルダへ保存)
    //     String fileName = null;
    //     try {
    //         String uploadDir = Paths.get(System.getProperty("user.dir"), "uploads").toAbsolutePath().toString();
    //         File dir = new File(uploadDir);
    //         if (!dir.exists()) {
    //             dir.mkdirs();
    //         }

    //         String originalFilename = imageFile.getOriginalFilename();
    //         String extension = "";
    //         if (originalFilename != null && originalFilename.contains(".")) {
    //             extension = originalFilename.substring(originalFilename.lastIndexOf("."));
    //         }
    //         fileName = UUID.randomUUID().toString() + extension;

    //         File dest = new File(dir, fileName);
    //         imageFile.transferTo(dest);

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         return ResponseEntity.internalServerError().body(Map.of("messages", List.of("画像の保存に失敗しました")));
    //     }

    //     // 4. Entity への詰め替え
    //     PrototypeEntity prototype = new PrototypeEntity();
    //     prototype.setTitle(prototypeForm.getTitle());
    //     prototype.setCatchcopy(prototypeForm.getCatchcopy());
    //     prototype.setConcept(prototypeForm.getConcept());
    //     prototype.setImage(fileName);

    //     // // ログインユーザーのセット
    //     // if (currentUser != null && currentUser.getUser() != null) {
    //     //     prototype.setUser(currentUser.getUser());
    //     // } else {
    //     //     UserEntity dummyUser = new UserEntity();
    //     //     dummyUser.setId(1);
    //     //     prototype.setUser(dummyUser);
    //     // }

    //     UserEntity dummyUser = new UserEntity();
    //     dummyUser.setId(1);
    //     prototype.setUser(dummyUser);

    //     // 5. DB 登録
    //     try {
    //         prototypeRepository.insert(prototype);
    //         return ResponseEntity.ok().body(prototype);
    //     } catch (Exception e) {
    //         System.out.println("エラー: " + e);
    //         return ResponseEntity.internalServerError().body(Map.of("messages", List.of("Internal Server Error")));
    //     }
    // }

    @PostMapping
    public ResponseEntity<?> createPrototype(
        @Validated @ModelAttribute PrototypeForm prototypeForm,
        BindingResult bindingResult,
        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) {
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

            // ダミーユーザーのセット（認証機能と連携前の場合）
            UserEntity dummyUser = new UserEntity();
            dummyUser.setId(1);
            entity.setUser(dummyUser);

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

}
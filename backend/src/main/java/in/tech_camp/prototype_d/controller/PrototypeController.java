package in.tech_camp.prototype_d.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.prototype_d.custom_user.CustomUserDetail;
import in.tech_camp.prototype_d.entity.PrototypeEntity;
import in.tech_camp.prototype_d.entity.UserEntity;
import in.tech_camp.prototype_d.form.PrototypeForm;
import in.tech_camp.prototype_d.form.CommentForm;
import in.tech_camp.prototype_d.form.SearchForm;
import in.tech_camp.prototype_d.repository.PrototypeRepository;
import in.tech_camp.prototype_d.repository.UserRepository;
import in.tech_camp.prototype_d.validation.ValidationOrder;
import lombok.AllArgsConstructor;

import java.util.Map; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.RequestBody; 

// @RestController
// @AllArgsConstructor
// @RequestMapping("/api/prototypes") 
// @CrossOrigin(origins = "http://localhost:3000")
// public class PrototypeController {

//     private final PrototypeRepository prototypeRepository;
//     private final UserRepository userRepository;

//     @GetMapping
//     public ResponseEntity<List<PrototypeEntity>> getAllPrototypes() {
//         try {
//             List<PrototypeEntity> prototypes = prototypeRepository.findAll();
//             return ResponseEntity.ok(prototypes);
//         } catch (Exception e) {
//             System.out.println("エラー: " + e);
//             return ResponseEntity.internalServerError().build();
//         }
//     }

//     // @PostMapping("/")
//     // public ResponseEntity<?> createPrototype(
//     //         @RequestBody @Validated(ValidationOrder.class) PrototypeForm prototypeForm,
//     //         BindingResult result,
//     //         @AuthenticationPrincipal CustomUserDetail currentUser) {

//     //     if (result.hasErrors()) {
//     //         List<String> errorMessages = result.getAllErrors().stream()
//     //                 .map(DefaultMessageSourceResolvable::getDefaultMessage)
//     //                 .collect(Collectors.toList());
//     //         return ResponseEntity.badRequest().body(Map.of("messages", errorMessages));
//     //     }                           

//     //     PrototypeEntity prototype = new PrototypeEntity();
//     //     prototype.setTitle(prototypeForm.getTitle());
//     //     prototype.setCatchcopy(prototypeForm.getCatchcopy());
//     //     prototype.setConcept(prototypeForm.getConcept());
//     //     prototype.setImage(prototypeForm.getImage());

//     //     try {
//     //         prototypeRepository.insert(prototype);
//     //         return ResponseEntity.ok().body(prototype);
//     //     } catch (Exception e) {
//     //         System.out.println("エラー: " + e);
//     //         return ResponseEntity.internalServerError().body(Map.of("messages", List.of("Internal Server Error")));
//     //     }
//     // }

//     @PostMapping
//     public ResponseEntity<?> createPrototype(
//             @RequestBody @Validated(ValidationOrder.class) PrototypeForm prototypeForm,
//             BindingResult result,
//             @AuthenticationPrincipal CustomUserDetail currentUser) {

//         if (result.hasErrors()) {
//             List<String> errorMessages = result.getAllErrors().stream()
//                     .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                     .collect(Collectors.toList());
//             return ResponseEntity.badRequest().body(Map.of("messages", errorMessages));
//         }                           

//         PrototypeEntity prototype = new PrototypeEntity();
//         prototype.setTitle(prototypeForm.getTitle());
//         prototype.setCatchcopy(prototypeForm.getCatchcopy());
//         prototype.setConcept(prototypeForm.getConcept());
//         prototype.setImage(prototypeForm.getImage());

//         // --------------------------------------------------
//         // ★ ここを追加！
//         // --------------------------------------------------
//         // ログイン機能が未完成（currentUserがnull）でも動作するように仮のユーザーIDをセットします
//         if (currentUser != null && currentUser.getUser() != null) {
//             // ログイン済みの場合はそのユーザーをセット
//             prototype.setUser(currentUser.getUser());
//         } else {
//             // 未ログイン（テスト時）は DB に実在する ID: 1 のユーザーを仮セット
//             UserEntity dummyUser = new UserEntity();
//             dummyUser.setId(1); // ※PostgreSQLのusersテーブルに存在するidを指定してください
//             prototype.setUser(dummyUser);
//         }
//         // --------------------------------------------------

//         try {
//             prototypeRepository.insert(prototype);
//             return ResponseEntity.ok().body(prototype);
//         } catch (Exception e) {
//             System.out.println("エラー: " + e);
//             return ResponseEntity.internalServerError().body(Map.of("messages", List.of("Internal Server Error")));
//         }
//     }
// }

import java.io.File;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/prototypes")
@CrossOrigin(origins = "http://localhost:3000")
public class PrototypeController {

    private final PrototypeRepository prototypeRepository;

    @GetMapping
    public ResponseEntity<List<PrototypeEntity>> getAllPrototypes() {
        try {
            List<PrototypeEntity> prototypes = prototypeRepository.findAll();
            return ResponseEntity.ok(prototypes);
        } catch (Exception e) {
            System.out.println("エラー: " + e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createPrototype(
            // ★ @RequestBody から @ModelAttribute に変更し、画像ファイル(file)を受け取れるようにする
            @ModelAttribute @Validated(ValidationOrder.class) PrototypeForm prototypeForm,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            BindingResult result,
            @AuthenticationPrincipal CustomUserDetail currentUser) {

        // 1. バリデーションエラー判定（@NotBlank や @Size チェック）
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(Map.of("messages", errorMessages));
        }

        // 画像ファイルが必須な場合はここでチェック（必要に応じて）
        if (imageFile == null || imageFile.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("messages", List.of("画像ファイルを選択してください")));
        }

        // 2. 画像の保存処理 (uploads/ フォルダへ保存)
        String fileName = null;
        try {
            // 保存先ディレクトリの設定
            String uploadDir = Paths.get(System.getProperty("user.dir"), "uploads").toAbsolutePath().toString();
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs(); // uploadsフォルダが無ければ自動作成
            }

            // 元の拡張子を取得し、UUIDで衝突しないファイル名を生成
            String originalFilename = imageFile.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            fileName = UUID.randomUUID().toString() + extension;

            // ディレクトリへファイルを転送保存
            File dest = new File(dir, fileName);
            imageFile.transferTo(dest);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("messages", List.of("画像の保存に失敗しました")));
        }

        // 3. Entity への詰め替え (DBには生成したファイル名のみを保持)
        PrototypeEntity prototype = new PrototypeEntity();
        prototype.setTitle(prototypeForm.getTitle());
        prototype.setCatchcopy(prototypeForm.getCatchcopy());
        prototype.setConcept(prototypeForm.getConcept());
        prototype.setImage(fileName); // ★ UUIDファイル名をDB登録用プロパティにセット

        // ログイン状態の判定
        if (currentUser != null && currentUser.getUser() != null) {
            prototype.setUser(currentUser.getUser());
        } else {
            UserEntity dummyUser = new UserEntity();
            dummyUser.setId(1);
            prototype.setUser(dummyUser);
        }

        // 4. DB 登録
        try {
            prototypeRepository.insert(prototype);
            return ResponseEntity.ok().body(prototype);
        } catch (Exception e) {
            System.out.println("エラー: " + e);
            return ResponseEntity.internalServerError().body(Map.of("messages", List.of("Internal Server Error")));
        }
    }
}
package in.tech_camp.prototype_d.controller;


import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import in.tech_camp.prototype_d.custom_user.CustomUserDetail;
import in.tech_camp.prototype_d.dto.PrototypeDto;
import in.tech_camp.prototype_d.dto.PrototypeListDto;
import in.tech_camp.prototype_d.service.LikeService;
import in.tech_camp.prototype_d.dto.PrototypeStatusDto;
import in.tech_camp.prototype_d.service.PrototypeService;
import in.tech_camp.prototype_d.validation.ValidationOrder;
import lombok.RequiredArgsConstructor;
import in.tech_camp.prototype_d.entity.PrototypeEntity;
import in.tech_camp.prototype_d.form.PrototypeForm;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PrototypeController {

  private final PrototypeService prototypeService;

  private final LikeService likeService;
  
  // プロトタイプ一覧表示
  @GetMapping({"/prototypes", "/", ""})
  public ResponseEntity<?> getPrototypes(@AuthenticationPrincipal CustomUserDetail currentUser) {
    try {
      Long userId = (currentUser != null) ? currentUser.getId() : null;
      List<PrototypeListDto> prototypes = prototypeService.getPrototypes(userId);
      return ResponseEntity.ok().body(prototypes);
    } catch (Exception e) {
      e.printStackTrace(); // エラーあったら見たい
      return ResponseEntity.internalServerError().body(Map.of("messages", List.of("投稿の取得に失敗しました。")));
    }
  }

  // プロトタイプ詳細表示
  @GetMapping("/prototypes/{prototypeId}")
  public ResponseEntity<?> showPrototypeDetail(@PathVariable("prototypeId") Long prototypeId, @AuthenticationPrincipal CustomUserDetail currentUser) {
    try {
      Long userId = (currentUser != null) ? currentUser.getId() : null;
      
      PrototypeDto prototype = prototypeService.getPrototypeById(prototypeId, userId);
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
  @DeleteMapping("/prototypes/{prototypeId}")
  public ResponseEntity<?> deletePrototype(@PathVariable("prototypeId") Long prototypeId, 
                                          @AuthenticationPrincipal CustomUserDetail currentUser) {
    try {
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
  
    //編集前画面に表示
  @GetMapping("/prototypes/{prototypeId}/edit")
  public ResponseEntity<?> editPrototype(@PathVariable ("prototypeId") Long prototypeId,
                                         @AuthenticationPrincipal CustomUserDetail currentUser) {
    try {
      PrototypeDto dto = prototypeService.getPrototypeForEdit(currentUser.getId(), prototypeId); //サービスのRepoから一件だけのデータを取得するメソッドを使ってＤＢからもってくる


      if  (dto == null) {
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(dto); //取得できたら表示

    }catch (IllegalArgumentException e) {
      return ResponseEntity.status(403).body(Map.of("Messages", List.of(e.getMessage())));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().body("サーバーエラーが発生しました");
    }
  }
//更新処理
    @PostMapping("/prototypes/{prototypeId}")
    public ResponseEntity<?> updatePrototype(@PathVariable ("prototypeId") Long prototypeId, 
                                             @AuthenticationPrincipal CustomUserDetail currentUser,
                                            @RequestPart("dto") PrototypeDto dto,
                                            @RequestPart(value = "image", required = false) MultipartFile imageFile
                                           ) { 
      try {
        prototypeService.updatePrototype(prototypeId, dto, imageFile, currentUser.getId());
        return ResponseEntity.ok().body(Map.of("Message", "プロトタイプを保存しました", "savePrototypeId", prototypeId
      ));
      } catch (IllegalArgumentException e){
        return ResponseEntity.status(403).body(Map.of("Messages", List.of(e.getMessage())));
      } catch (Exception e) { 
        e.printStackTrace();
        return ResponseEntity.internalServerError().body("サーバーエラーが発生しました");
 
      }
    }
      

   /**
     * プロトタイプ新規作成 (画像アップロード含む)
     * POST: /api/prototypes
     */
    @PostMapping("/prototypes")
public ResponseEntity<?> createPrototype(
    @Validated(ValidationOrder.class) @ModelAttribute PrototypeForm prototypeForm,
    BindingResult bindingResult,
    @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
    @AuthenticationPrincipal CustomUserDetail customUserDetails
) {
    // 未ログインチェック
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
        // 3. サービスクラスに登録処理を一任
        Long currentUserId = customUserDetails.getUser().getId();
        PrototypeEntity savedEntity = prototypeService.createPrototype(prototypeForm, imageFile, currentUserId);

        return ResponseEntity.ok().body(savedEntity);

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

  @GetMapping("/prototype/today")
  public ResponseEntity<?> showPrototypeToday(@AuthenticationPrincipal CustomUserDetail currentUser) {
    try {
      Map<String, Object> responseData = prototypeService.getPrototypeToday(currentUser);
      if(responseData.get("prototype") == null){
        return ResponseEntity.notFound().build(); 
      }
        return ResponseEntity.ok().body(responseData);
      } catch (Exception e) {
        System.out.println("エラー :" + e);
    
      return ResponseEntity.internalServerError().body("サーバーエラー");
    }
  }

  //いいね追加
  @PostMapping("/prototypes/{prototypeId}/likes")
  public ResponseEntity<?> addLikeToPrototype(@PathVariable ("prototypeId") Long prototypeId, @AuthenticationPrincipal CustomUserDetail currentUser) {
  
    try{
        if (currentUser == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ログインが必要です");
      }
      Long userId = currentUser.getId();

      likeService.toggleLike(prototypeId, userId);

      return ResponseEntity.ok().build();
    } catch ( RuntimeException e ) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

  }
  //いいね順
  @GetMapping("/prototypes/likes")
  public ResponseEntity<?> getPrototypeOrderByLikes(@AuthenticationPrincipal CustomUserDetail currentUser) {
    try{
      Long userId = (currentUser != null) ? currentUser.getId() : null;
   List<PrototypeListDto> prototypes = likeService.getPrototypeOrderByLikes(userId);
    return ResponseEntity.ok().body(prototypes);
  } catch (NullPointerException e){
    e.printStackTrace();
    return ResponseEntity.internalServerError().body(Map.of("messages", List.of("いいね順の取得に失敗しました。")));
  }  }
  
  
  
    // プロトタイプgacha取得
  @GetMapping("/prototype/gacha")
  public ResponseEntity<?> showPrototypeRandom() {
    try {
      PrototypeStatusDto prototype = prototypeService.getPrototypeRandom();
      if(prototype == null){
        return ResponseEntity.notFound().build(); 
      }
        return ResponseEntity.ok().body(prototype);
      } catch (Exception e) {
        System.out.println("エラー :" + e);
    
      return ResponseEntity.internalServerError().body("サーバーエラー");
    }
  }
}

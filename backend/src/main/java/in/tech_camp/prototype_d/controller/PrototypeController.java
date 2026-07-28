package in.tech_camp.prototype_d.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.tech_camp.prototype_d.custom_user.CustomUserDetail;
import in.tech_camp.prototype_d.dto.PrototypeDto;
import in.tech_camp.prototype_d.service.PrototypeService;
import lombok.RequiredArgsConstructor;
import in.tech_camp.prototype_d.repository.PrototypeRepository;
import in.tech_camp.prototype_d.entity.PrototypeEntity;


import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PrototypeController {

  private final PrototypeService prototypeService;

  private final PrototypeRepository prototypeRepository;

  // プロトタイプ一覧表示
  @GetMapping({"/prototypes", "/", ""})
  public ResponseEntity<?> getPrototypes() {
    try {
      List<PrototypeDto> prototypes = prototypeService.getPrototypes();
      return ResponseEntity.ok().body(prototypes);
    } catch (Exception e) {
      e.printStackTrace(); // エラーあったら見たい
      return ResponseEntity.internalServerError().body(Map.of("messages", List.of("投稿の取得に失敗しました。")));
    }
  }

  // プロトタイプ詳細表示
  @GetMapping("/prototypes/{prototypeId}")
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
    public ResponseEntity<?> updatePrototype(@PathVariable ("prototypeId") Long prototypeId, @RequestBody PrototypeDto dto,
                                             @AuthenticationPrincipal CustomUserDetail currentUser) { 
      try {
        prototypeService.updatePrototype(prototypeId, dto, currentUser.getId());
        return ResponseEntity.ok().body(Map.of("Message", "プロトタイプを保存しました", "savePrototypeId", prototypeId
      ));
      } catch (IllegalArgumentException e){
        return ResponseEntity.status(403).body(Map.of("Messages", List.of(e.getMessage())));
      } catch (Exception e) { 
        e.printStackTrace();
        return ResponseEntity.internalServerError().body("サーバーエラーが発生しました");
 
      }
    }
}

package in.tech_camp.prototype_d.controller;

import org.springframework.web.bind.annotation.RestController;

import in.tech_camp.prototype_d.dto.CommentDto;
import in.tech_camp.prototype_d.form.CommentForm;
import in.tech_camp.prototype_d.service.CommentService;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
public class CommentController {
  
  private final CommentService commentService;

  // コメント一覧を取得
  @GetMapping("/prototypes/{prototypeId}/comments")
  public ResponseEntity<List<CommentDto>> getComments(@PathVariable("prototypeId") Long prototypeId) {
      List<CommentDto> comments = commentService.getCommentsByPrototypeId(prototypeId);
      return ResponseEntity.ok(comments);
  }

  // コメントを保存
  @PostMapping("/prototypes/{protoTypeId}/comments")
  public ResponseEntity<?> createComment(@PathVariable("prototypeId") Long prototypeId,
                                          @RequestBody @Validated CommentForm commentForm,
                                          BindingResult result
                                          // @AuthenticationPrincipal CustomUserDetail currentUser
                                        ) {
                                            
    // バリデーションエラー処理
    if (result.hasErrors()) {
        System.out.println("バリデーションエラーが発生中: " + result.getAllErrors());
        
        // エラー内容をJSONでフロントエンドに返す
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    // DBに保存
    try {
        // ユーザー機能実装前なので1Lを設定
        Long userId = 1L;
        // Long userId = (currentUser != null && currentUser.getUser() != null) 
        //         ? currentUser.getUser().getId() 
        //         : 1L;

        commentService.saveComment(prototypeId, userId, commentForm.getComment());
        
        return ResponseEntity.ok(Map.of("message", "コメントを投稿しました"));

    } catch (Exception e) {
        System.out.println("エラー：" + e);
        return ResponseEntity.internalServerError().body(Map.of("message", "サーバーエラーが発生しました"));
    }
  }
}
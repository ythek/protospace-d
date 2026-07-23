package in.tech_camp.prototype_d.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentForm {
  // もしかしたらエラーメッセージがフロントとバックで二重になるかも
  @NotBlank(message = "コメントを入力してください")
  private String comment;
}

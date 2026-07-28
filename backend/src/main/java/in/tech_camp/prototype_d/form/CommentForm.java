package in.tech_camp.prototype_d.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentForm {

    @NotNull(message = "プロトタイプIDが不正です")
    private Long prototypeId;

    @NotBlank(message = "コメントを入力してください")
    private String comment;
}
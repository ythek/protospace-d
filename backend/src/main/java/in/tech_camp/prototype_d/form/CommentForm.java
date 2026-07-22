package in.tech_camp.prototype_d.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentForm {

    @NotBlank(message = "コメントを入力してください")
    @Size(max = 128, message = "コメントは128文字以内で入力してください")
    private String text;

    @NotNull(message = "プロトタイプIDが不正です")
    private Integer prototypeId;
}
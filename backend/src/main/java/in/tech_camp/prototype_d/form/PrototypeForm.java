package in.tech_camp.prototype_d.form;

import in.tech_camp.prototype_d.validation.ValidationPriority1;
import in.tech_camp.prototype_d.validation.ValidationPriority2;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PrototypeForm {

    @NotBlank(message = "タイトルを入力してください", groups = ValidationPriority1.class)
    @Size(max = 128, message = "タイトルは128文字以内で入力してください", groups = ValidationPriority2.class)
    private String title;

    @NotBlank(message = "キャッチコピーを入力してください", groups = ValidationPriority1.class)
    @Size(max = 128, message = "キャッチコピーは128文字以内で入力してください", groups = ValidationPriority2.class)
    private String catchcopy;

    @NotBlank(message = "コンセプトを入力してください", groups = ValidationPriority1.class)
    @Size(max = 128, message = "コンセプトは128文字以内で入力してください", groups = ValidationPriority2.class)
    private String concept;

    private String image;

}
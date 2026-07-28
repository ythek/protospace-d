package in.tech_camp.prototype_d.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PrototypeForm {

    @NotBlank(message = "タイトルを入力してください")
    @Size(max = 128, message = "タイトルは128文字以内で入力してください")
    private String title;

    @NotBlank(message = "キャッチコピーを入力してください")
    @Size(max = 128, message = "キャッチコピーは128文字以内で入力してください")
    private String catchcopy;

    @NotBlank(message = "コンセプトを入力してください")
    @Size(max = 128, message = "コンセプトは128文字以内で入力してください")
    private String concept;

    private String image;

}
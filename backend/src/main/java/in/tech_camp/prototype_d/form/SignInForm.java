package in.tech_camp.prototype_d.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInForm {

    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "メールアドレスは@を含む形式で入力してください")
    private String email; 

    @NotBlank(message = "パスワードを入力してください")
    private String password;
}
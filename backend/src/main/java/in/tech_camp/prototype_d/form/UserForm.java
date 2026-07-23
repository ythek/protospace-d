package in.tech_camp.prototype_d.form;

import org.hibernate.validator.constraints.Length;
import org.springframework.validation.BindingResult;

import in.tech_camp.prototype_d.validation.ValidationPriority1;
import in.tech_camp.prototype_d.validation.ValidationPriority2;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class UserForm {
  @NotBlank(message = "Email cannot be blank", groups = ValidationPriority1.class)
  @Email(message = "Email should be valid", groups = ValidationPriority2.class)
  private String email;

  @NotBlank(message = "Password cannot be blank", groups = ValidationPriority1.class)
  @Length(min = 6, max = 128, message = "Password should be between 6 and 128 characters", groups = ValidationPriority2.class)
  private String password;

  private String passwordConfirmation;

  @NotBlank(message = "username cannot be blank", groups = ValidationPriority1.class)
  @Length(max = 128, message = "username should be shorter than 129 characters", groups = ValidationPriority2.class)
  private String username;

  @NotBlank(message = "profile cannot be blank", groups = ValidationPriority1.class)
  @Length(max = 128, message = "profile should be shorter than 129 characters", groups = ValidationPriority2.class)
  private String profile;

  @NotBlank(message = "affiliation cannot be blank", groups = ValidationPriority1.class)
  @Length(max = 128, message = "affiliation should be shorter than 129 characters", groups = ValidationPriority2.class)
  private String affiliation;

  @NotBlank(message = "position cannot be blank", groups = ValidationPriority1.class)
  @Length(max = 128, message = "position should be shorter than 129 characters", groups = ValidationPriority2.class)
  private String position;

  public void validatePasswordConfirmation(BindingResult result) {
      if (!password.equals(passwordConfirmation)) {
          result.rejectValue("passwordConfirmation", null, "Password confirmation doesn't match Password");
      }
  }
}

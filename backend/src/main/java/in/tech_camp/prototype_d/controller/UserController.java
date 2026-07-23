package in.tech_camp.prototype_d.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.tech_camp.prototype_d.entity.UserEntity;
import in.tech_camp.prototype_d.form.UserForm;
import in.tech_camp.prototype_d.repository.UserRepository;
import in.tech_camp.prototype_d.service.UserService;
import in.tech_camp.prototype_d.validation.ValidationOrder;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final UserRepository userRepository;

  // ユーザー登録
  @PostMapping("/")
  public ResponseEntity<?> createUser(@RequestBody  @Validated(ValidationOrder.class) UserForm userForm, BindingResult result){

    // パスワードのチェック
    userForm.validatePasswordConfirmation(result);
    // emailの一意性チェック
    if (userRepository.existsByEmail(userForm.getEmail())) {
      result.rejectValue("email", "null", "Email already exists");
    }

    try {
      UserEntity userEntity = userService.registerUser(userForm);
      return ResponseEntity.ok().body(Map.of(
        "id", userEntity.getId(),
        "username", userEntity.getUsername()
      ));
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return ResponseEntity.internalServerError().body(Map.of("messages", List.of("Internal Server Error")));    
    }
  }
}
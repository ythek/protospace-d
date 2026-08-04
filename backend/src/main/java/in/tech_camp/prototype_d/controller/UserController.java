package in.tech_camp.prototype_d.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.tech_camp.prototype_d.dto.PrototypeListDto;
import in.tech_camp.prototype_d.dto.UserDetailDto;
import in.tech_camp.prototype_d.entity.UserEntity;
import in.tech_camp.prototype_d.form.SignInForm;
import in.tech_camp.prototype_d.form.UserForm;
import in.tech_camp.prototype_d.repository.UserRepository;
import in.tech_camp.prototype_d.service.PrototypeService;
import in.tech_camp.prototype_d.service.UserService;
import in.tech_camp.prototype_d.validation.ValidationOrder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final UserRepository userRepository;
  private final PrototypeService prototypeService;

  // ユーザー登録
  @PostMapping("/sign_up")
  public ResponseEntity<?> createUser(@RequestBody  @Validated(ValidationOrder.class) UserForm userForm, BindingResult result, HttpServletRequest request, HttpServletResponse response){

    // パスワードのチェック
    userForm.validatePasswordConfirmation(result);
    // emailの一意性チェック
    if (userRepository.existsByEmail(userForm.getEmail())) {
      result.rejectValue("email", "null", "Email already exists");
    }

    try {
      UserEntity userEntity = userService.registerUser(userForm, request, response );
      return ResponseEntity.ok().body(Map.of(
        "id", userEntity.getId(),
        "username", userEntity.getUsername()
      ));
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return ResponseEntity.internalServerError().body(Map.of("messages", List.of("Internal Server Error")));    
    }
  }

  // 指定されたIdのユーザーを取得
  @GetMapping({"/{userId}"})
  public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
      UserDetailDto userDetailDto = userService.getUser(userId);
      List<PrototypeListDto> prototypes = prototypeService.getPrototypesByUserId(userId);

      Map<String, Object> responseData = new HashMap<>();
        responseData.put("user", userDetailDto);
        responseData.put("prototypes", prototypes);
      return ResponseEntity.ok().body(responseData);
    } catch (Exception e) {
      e.printStackTrace(); 
      return ResponseEntity.internalServerError().body(Map.of("messages", List.of("取得に失敗しました。")));
    }
  }

  @PostMapping("/sign_in")
public ResponseEntity<?> signIn(
    @RequestBody @Validated SignInForm signInForm, 
    BindingResult result,
    HttpServletRequest request,
    HttpServletResponse response) {

  // 1. バリデーションエラー（@がない、空欄など）のチェック
  if (result.hasErrors()) {
    List<String> errorMessages = result.getAllErrors().stream()
        .map(error -> error.getDefaultMessage())
        .toList();
    return ResponseEntity.badRequest().body(Map.of("messages", errorMessages));
  }

  // 2. ログイン処理を実行
  try {
    UserEntity userEntity = userService.loginUser(signInForm, request, response);
    
    // DTOを使わず、Mapで必要な値だけを返却
    return ResponseEntity.ok().body(Map.of(
      "id", userEntity.getId(),
      "username", userEntity.getUsername()
    ));
  } catch (Exception e) {
    return ResponseEntity.status(401).body(Map.of("messages", List.of("メールアドレスまたはパスワードが正しくありません")));
  }
}
}

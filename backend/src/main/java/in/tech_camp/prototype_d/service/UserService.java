package in.tech_camp.prototype_d.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.prototype_d.dto.UserDetailDto;
import in.tech_camp.prototype_d.entity.AffiliationEntity;
import in.tech_camp.prototype_d.entity.PositionEntity;
import in.tech_camp.prototype_d.entity.UserEntity;
import in.tech_camp.prototype_d.form.UserForm;
import in.tech_camp.prototype_d.repository.AffiliationRepository;
import in.tech_camp.prototype_d.repository.PositionRepository;
import in.tech_camp.prototype_d.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final AffiliationRepository affiliationRepository;
  private final PositionRepository positionRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserAuthenticationService userAuthenticationService;

  @Transactional
      public UserEntity registerUser(UserForm userForm, HttpServletRequest request, HttpServletResponse response) {

          //所属（affiliation）テーブルへ登録
          Long affiliationId = affiliationRepository.findIdByName(userForm.getAffiliation());
          if (affiliationId == null) {
              AffiliationEntity affiliation = new AffiliationEntity();
              affiliation.setAffiliationName(userForm.getAffiliation());
              affiliationRepository.insert(affiliation); // ここでDBに保存され、IDが発行される
              affiliationId = affiliation.getId(); // 発行されたIDを取得
          }
          
          //役職（Position）テーブルへ登録
          Long positionId = positionRepository.findIdByName(userForm.getPosition());
          if (positionId == null) {
              PositionEntity position = new PositionEntity();
              position.setPositionName(userForm.getPosition());
              positionRepository.insert(position); // ここでDBに保存され、IDが発行される
              positionId = position.getId(); // 発行されたIDを取得
          }

          createUserWithEncryptedPassword(userForm);

          UserEntity userEntity = new UserEntity();
          userEntity.setEmail(userForm.getEmail());
          userEntity.setPassword(userForm.getPassword());
          userEntity.setUsername(userForm.getUsername());
          userEntity.setProfile(userForm.getProfile());
          userEntity.setAffiliationId(affiliationId);
          userEntity.setPositionId(positionId);
          userRepository.insert(userEntity);

          autoLogin(userEntity.getEmail(), request, response);
          return userEntity;
      }

  private void createUserWithEncryptedPassword(UserForm userForm) {
    String encodedPassword = encodePassword(userForm.getPassword());
    userForm.setPassword(encodedPassword);
    }

  private String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }


  // Dtoに詰め替える
    public UserDetailDto getUser(Long userId) {

    UserEntity userEntity = userRepository.findById(userId);
      UserDetailDto dto = new UserDetailDto();    
    if(userEntity != null){
      dto.setId(userEntity.getId());
      dto.setUsername(userEntity.getUsername());
      dto.setProfile(userEntity.getProfile());
      dto.setAffiliation(affiliationRepository.findNameById(userEntity.getAffiliationId()));
      dto.setPosition(positionRepository.findNameById(userEntity.getPositionId()));
    }else{
      dto.setId(null);
    }
    return dto;
  }

  // 新規登録後にログイン状態
  public void autoLogin(String email, HttpServletRequest request, HttpServletResponse response) {    
        UserDetails userDetails = userAuthenticationService.loadUserByUsername(email);
 
        // 認証トークンを作成
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        
        // 認証情報をセット
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        // Cookieに保存する
        SecurityContextRepository contextRepository = new HttpSessionSecurityContextRepository();
        contextRepository.saveContext(context, request, response);
    }
}
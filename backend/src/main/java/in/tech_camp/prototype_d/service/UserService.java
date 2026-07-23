package in.tech_camp.prototype_d.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.prototype_d.entity.AffiliationEntity;
import in.tech_camp.prototype_d.entity.PositionEntity;
import in.tech_camp.prototype_d.entity.UserEntity;
import in.tech_camp.prototype_d.form.UserForm;
import in.tech_camp.prototype_d.repository.AffiliationRepository;
import in.tech_camp.prototype_d.repository.PositionRepository;
import in.tech_camp.prototype_d.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final AffiliationRepository affiliationRepository;
  private final PositionRepository positionRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
      public UserEntity registerUser(UserForm userForm) {

          //所属（affiliation）テーブルへ登録
          Integer affiliationId = affiliationRepository.findIdByName(userForm.getAffiliation());
          if (affiliationId == null) {
              AffiliationEntity affiliation = new AffiliationEntity();
              affiliation.setAffiliationName(userForm.getAffiliation());
              affiliationRepository.insert(affiliation); // ここでDBに保存され、IDが発行される
              affiliationId = affiliation.getId(); // 発行されたIDを取得
          }
          
          //役職（Position）テーブルへ登録
          Integer positionId = positionRepository.findIdByName(userForm.getPosition());
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
          return userEntity;
      }

  private void createUserWithEncryptedPassword(UserForm userForm) {
    String encodedPassword = encodePassword(userForm.getPassword());
    userForm.setPassword(encodedPassword);
    }

  private String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }
}
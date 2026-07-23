package in.tech_camp.prototype_d.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.tech_camp.prototype_d.entity.AffiliationEntity;
import in.tech_camp.prototype_d.entity.PositionEntity;
import in.tech_camp.prototype_d.entity.UserEntity;
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

  public void createUserWithEncryptedPassword(UserEntity userEntity) {
    String encodedPassword = encodePassword(userEntity.getPassword());
    userEntity.setPassword(encodedPassword);

    // 所属のIDを取得（無ければ新規作成)
    Integer affiliationId = affiliationRepository.findIdByName(userEntity.getAffiliation());
        if (affiliationId == null) {
            AffiliationEntity newAffiliation = new AffiliationEntity();
            newAffiliation.setAffiliationName(userEntity.getAffiliation());
            affiliationRepository.insert(newAffiliation); // ここでDBに保存され、IDが発行される
            affiliationId = newAffiliation.getId(); // 発行されたIDを取得
        }

    // 役職のIDを取得（無ければ新規作成）
    Integer positionId = positionRepository.findIdByName(userEntity.getPosition());
        if (positionId == null) {
            PositionEntity newPosition = new PositionEntity();
            newPosition.setPositionName(userEntity.getPosition());
            positionRepository.insert(newPosition); // ここでDBに保存され、IDが発行される
            positionId = newPosition.getId(); // 発行されたIDを取得
        }
    userEntity.setAffiliationId(affiliationId);
    userEntity.setPositionId(positionId);
    userRepository.insert(userEntity);
  }

  private String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }
}
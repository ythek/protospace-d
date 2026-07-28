package in.tech_camp.prototype_d.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.prototype_d.dto.PrototypeDto;
import in.tech_camp.prototype_d.dto.UserDto;
import in.tech_camp.prototype_d.entity.PrototypeEntity;
import in.tech_camp.prototype_d.repository.PrototypeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrototypeService {

  private final PrototypeRepository prototypeRepository;

  // いずれ使うので書いておく
  // private final UserRepository userRepository;

  // 全件取得
  public List<PrototypeDto> getPrototypes() {
    List<PrototypeEntity> entities = prototypeRepository.findAll();
    List<PrototypeDto> dtos = new ArrayList<>();

    for (PrototypeEntity entity : entities) {
      PrototypeDto dto = new PrototypeDto();
      dto.setId(entity.getId());
      dto.setTitle(entity.getTitle());
      dto.setCatchcopy(entity.getCatchcopy());
      dto.setConcept(entity.getConcept());
      dto.setImage(entity.getImage());

      UserDto userDto = new UserDto();
      
      // 本来はentity.getUserId()を使ってDBからユーザー情報を取得する
      // UserEntity user = userRepository.findById(entity.getUserId());
      
      // ダミーのユーザー名
      userDto.setUsername("testuser123");

      dto.setUser(userDto);
      dtos.add(dto);
    }

    return dtos;
  }


  //編集用データ
    public PrototypeDto getPrototypeForEdit(Long currentUserId, Long prototypeId) {

      Long ownerId = prototypeRepository.findUserIdById(prototypeId);

      if (ownerId == null){
        throw new IllegalArgumentException("対象のプロトタイプが見つかりません");
      }

      if (!ownerId.equals(currentUserId)) {
        throw new IllegalArgumentException("編集権限がありません");
      }
        PrototypeEntity entity = prototypeRepository.findById(prototypeId);
        
        if (entity == null) {
            return null;
        }

        PrototypeDto dto = new PrototypeDto();

        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setTitle(entity.getTitle());
        dto.setCatchcopy(entity.getCatchcopy());
        dto.setConcept(entity.getConcept());
        dto.setImage(entity.getImage());

        return dto;
    }

    //更新処理
    public void updatePrototype(Long prototypeId, PrototypeDto dto, Long currentUserId) {

      Long ownerId = prototypeRepository.findUserIdById(prototypeId);

      if (ownerId == null){
        throw new IllegalArgumentException("対象のプロトタイプが見つかりません");
      }

      if (!ownerId.equals(currentUserId)) {
        throw new IllegalArgumentException("編集権限がありません");
      }

        PrototypeEntity entity = new PrototypeEntity();
        
        entity.setId(prototypeId);
        entity.setTitle(dto.getTitle());
        entity.setCatchcopy(dto.getCatchcopy());
        entity.setConcept(dto.getConcept());
        entity.setImage(dto.getImage());

        prototypeRepository.update(entity);
    }
    // プロトタイプ削除機能
  @Transactional
  public void deletePrototype(Long prototypeId, Long currentUserId) {
    Long ownerId = prototypeRepository.findUserIdById(prototypeId);

    if (ownerId == null) {
        throw new IllegalArgumentException("対象のプロトタイプが見つかりません");
    }

    if (!ownerId.equals(currentUserId)) {
        throw new IllegalArgumentException("削除する権限がありません");
    }

    prototypeRepository.deletePrototype(prototypeId);
  }
}

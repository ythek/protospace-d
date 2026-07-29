package in.tech_camp.prototype_d.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.prototype_d.dto.PrototypeDto;
import in.tech_camp.prototype_d.dto.UserDto;
import in.tech_camp.prototype_d.entity.PrototypeEntity;
import in.tech_camp.prototype_d.entity.UserEntity;
import in.tech_camp.prototype_d.repository.PrototypeRepository;
import in.tech_camp.prototype_d.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrototypeService {

  private final PrototypeRepository prototypeRepository;
  private final UserRepository userRepository;

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

      // entity.getUserId()を使ってDBからユーザー情報を取得する
      UserEntity user = userRepository.findById(entity.getUserId());
      userDto.setUsername(user.getUsername());
      userDto.setId(user.getId());
      dto.setUser(userDto);
      dtos.add(dto);
    }

    return dtos;
  }

    public List<PrototypeDto> getPrototypesByUserId(Long userId) {
    List<PrototypeEntity> entities = prototypeRepository.findByUserId(userId);
    List<PrototypeDto> dtos = new ArrayList<>();

    for (PrototypeEntity entity : entities) {
      PrototypeDto dto = new PrototypeDto();
      dto.setId(entity.getId());
      dto.setTitle(entity.getTitle());
      dto.setCatchcopy(entity.getCatchcopy());
      dto.setConcept(entity.getConcept());
      dto.setImage(entity.getImage());

      UserDto userDto = new UserDto();
      
      // entity.getUserId()を使ってDBからユーザー情報を取得する
      UserEntity user = userRepository.findById(entity.getUserId());
      userDto.setUsername(user.getUsername());
      userDto.setId(user.getId());
      dto.setUser(userDto);
      dtos.add(dto);
    }

    return dtos;
  }

  public PrototypeDto getPrototypeById(Long id) {
    PrototypeEntity entity = prototypeRepository.findById(id);
    PrototypeDto dto = new PrototypeDto();
    if(entity != null){
      dto.setId(entity.getId());
      dto.setTitle(entity.getTitle());
      dto.setCatchcopy(entity.getCatchcopy());
      dto.setConcept(entity.getConcept());
      dto.setImage(entity.getImage());

      UserDto userDto = new UserDto();
      
      // entity.getUserId()を使ってDBからユーザー情報を取得する
      UserEntity user = userRepository.findById(entity.getUserId());
      userDto.setUsername(user.getUsername());
      userDto.setId(user.getId());
      dto.setUser(userDto);
    }else{
      dto = null;
    }
    return dto;
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
    public void updatePrototype(Long prototypeId, PrototypeDto dto, MultipartFile imageFile,Long currentUserId) {

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
        
        if (imageFile != null && !imageFile.isEmpty()) {
          try {
                // 保存先フォルダ（/uploads）を作成
                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                // 重複防止用ファイル名（例: uuid_sample.png）
                String savedFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

                // ファイルを保存
                imageFile.transferTo(new File(uploadDir + savedFileName));

                // 保存したファイル名を entity にセット
                entity.setImage(savedFileName);

            } catch (IOException e) {
                throw new RuntimeException("画像の保存に失敗しました", e);
            }
        } else {
            // 新しい画像が送られてこなかった場合は既存の画像名を保持
            if (dto.getImage() != null && !dto.getImage().trim().isEmpty()) {
            entity.setImage(dto.getImage());
        }
      }


        prototypeRepository.update(entity);
}
}
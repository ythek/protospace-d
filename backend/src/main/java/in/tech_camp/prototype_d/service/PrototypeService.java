package in.tech_camp.prototype_d.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

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

      dto.setUser(userDto);
      dtos.add(dto);
    }

    return dtos;
  }

    public List<PrototypeDto> getPrototypesByUserId(Integer userId) {
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

      dto.setUser(userDto);
      dtos.add(dto);
    }

    return dtos;
  }
}
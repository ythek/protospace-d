package in.tech_camp.prototype_d.service;

import java.util.ArrayList;
import java.util.List;

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
            // entity.getUser() が存在する場合はそのユーザー名をセット
            if (entity.getUser() != null) {
                userDto.setUsername(entity.getUser().getUsername());
            } else {
                userDto.setUsername("testuser123");
            }

            dto.setUser(userDto);
            dtos.add(dto);
        }

        return dtos;
    }

    // 新規追加：保存処理
    @Transactional
    public void insert(PrototypeEntity prototypeEntity) {
        prototypeRepository.insert(prototypeEntity);
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

        prototypeRepository.delete(prototypeId);
    }
}
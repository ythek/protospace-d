package in.tech_camp.prototype_d.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.tech_camp.prototype_d.entity.PrototypeEntity;
import in.tech_camp.prototype_d.repository.PrototypeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrototypeService {

  private final PrototypeRepository prototypeRepository;

  // 全件取得
  public List<PrototypeEntity> getPrototypes() {
    return prototypeRepository.findAll();
  }
}

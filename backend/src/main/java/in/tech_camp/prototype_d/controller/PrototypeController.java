package in.tech_camp.prototype_d.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.tech_camp.prototype_d.entity.PrototypeEntity;
import in.tech_camp.prototype_d.service.PrototypeService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PrototypeController {

  private final PrototypeService prototypeService;

  @GetMapping("/")
  public ResponseEntity<?> getPrototypes() {
    try {
      List<PrototypeEntity> prototypes = prototypeService.getPrototypes();
      return ResponseEntity.ok().body(prototypes);
    } catch (Exception e) {
      e.printStackTrace(); // エラーあったら見たい
      return ResponseEntity.internalServerError().body(Map.of("messages", List.of("投稿の取得に失敗しました。")));
    }
  }
}
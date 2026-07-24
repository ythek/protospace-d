package in.tech_camp.prototype_d.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.tech_camp.prototype_d.dto.PrototypeDto;
import in.tech_camp.prototype_d.service.PrototypeService;
import lombok.RequiredArgsConstructor;
import in.tech_camp.prototype_d.repository.PrototypeRepository;
import in.tech_camp.prototype_d.entity.PrototypeEntity;


import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PrototypeController {

  private final PrototypeService prototypeService;

  private final PrototypeRepository prototypeRepository;

  @GetMapping({"/prototypes", "/", ""})
  public ResponseEntity<?> getPrototypes() {
    try {
      List<PrototypeDto> prototypes = prototypeService.getPrototypes();
      return ResponseEntity.ok().body(prototypes);
    } catch (Exception e) {
      e.printStackTrace(); // エラーあったら見たい
      return ResponseEntity.internalServerError().body(Map.of("messages", List.of("投稿の取得に失敗しました。")));
    }
  }

  @GetMapping("/prototypes/{prototypeId}")
  public ResponseEntity<?> showPrototypeDetail(@PathVariable("prototypeId") Integer prototypeId) {
  try {
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    if(prototype == null){
      return ResponseEntity.notFound().build(); 
    }
      return ResponseEntity.ok().body(prototype);
    } catch (Exception e) {
      System.out.println("エラー :" + e);
  
    return ResponseEntity.internalServerError().body("サーバーエラー");
      
}
}
}
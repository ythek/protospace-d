package in.tech_camp.prototype_d.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LikeEntity {
  private Long id;
  private Long user_id;
  private Long prototype_id;
  private LocalDateTime createdAt;

}

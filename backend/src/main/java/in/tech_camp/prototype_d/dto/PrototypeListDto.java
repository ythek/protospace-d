package in.tech_camp.prototype_d.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PrototypeListDto {
  private Long id;
  private String title;
  private String catchcopy;
  private String image;
  private Long userId;
  private String username;
  private LocalDateTime createdAt;
  private boolean likecheck;
  private Long likecount;
}

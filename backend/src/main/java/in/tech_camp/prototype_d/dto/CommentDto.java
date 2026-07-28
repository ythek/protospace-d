package in.tech_camp.prototype_d.dto;

import lombok.Data;

@Data
public class CommentDto {
  private Long id;
  private String comment;
  private String username;
  private Long userId;
}

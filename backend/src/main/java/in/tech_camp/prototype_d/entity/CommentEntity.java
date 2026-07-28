package in.tech_camp.prototype_d.entity;

import lombok.Data;

@Data
public class CommentEntity {
  private Long id;
  private String comment;
  private Long userId;
  private Long prototypeId;
}

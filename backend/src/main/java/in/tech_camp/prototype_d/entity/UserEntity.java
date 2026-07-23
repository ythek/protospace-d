package in.tech_camp.prototype_d.entity;

// import java.util.List;

import lombok.Data;

@Data
public class UserEntity {
  private Integer id;
  private String username;
  private String email;
  private String password;
  private String profile;
  private String affiliation;
  private String position;
  private Integer affiliationId;
  private Integer positionId;
}

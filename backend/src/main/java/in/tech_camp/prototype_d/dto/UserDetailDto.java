package in.tech_camp.prototype_d.dto;

import lombok.Data;

@Data
public class UserDetailDto {
  private Long id;
  private String username;
  private String profile;
  private String affiliation;
  private String position;
}

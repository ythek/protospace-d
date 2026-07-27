package in.tech_camp.prototype_d.dto;

import lombok.Data;

@Data
public class PrototypeDto {
  private Integer id;
  private String title;
  private String catchcopy;
  private String concept;
  private String image;
  private UserDto user;
}

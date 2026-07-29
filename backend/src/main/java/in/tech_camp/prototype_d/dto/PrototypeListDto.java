package in.tech_camp.prototype_d.dto;

import lombok.Data;

@Data
public class PrototypeListDto {
  private Long id;
  private String title;
  private String catchcopy;
  private String image;
  private String username;
}
package in.tech_camp.prototype_d.dto;

import lombok.Data;

@Data
public class PrototypeStatusDto {
  private Long id;
  private String title;
  private String catchcopy;
  private String image;
  private Long userId;
  private String username;
  private Long rarity;
  private Long attack;
  private Long defense;
}
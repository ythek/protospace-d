package in.tech_camp.prototype_d.dto;

import lombok.Data;

@Data
public class ChatRoomDetailDto {
  private Long roomId;
  private String counterpartName;
  private String messages;
}

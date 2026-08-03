package in.tech_camp.prototype_d.dto;

import lombok.Data;

@Data
public class ChatRoomListDto {
  private Long roomId;
  private Long counterpartUserId;
  private String counterpartName;
  private String latestMessage;
}

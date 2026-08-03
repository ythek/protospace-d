package in.tech_camp.prototype_d.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatRoomMemberEntity {
  private Long id;
  private Long chatRoomId;
  private Long userId;
  private LocalDateTime joinedAt;
}
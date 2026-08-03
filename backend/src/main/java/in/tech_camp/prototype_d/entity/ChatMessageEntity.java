package in.tech_camp.prototype_d.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatMessageEntity {
  private Long id;
  private Long chatRoomId;
  private Long senderId;
  private String content;
  private LocalDateTime createdAt;
}
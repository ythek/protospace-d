package in.tech_camp.prototype_d.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatMessageResponseDto {
  private Long id;
  private Long chatRoomId;
  private Long senderId;
  private String content;
  private LocalDateTime createdAt;
}

package in.tech_camp.prototype_d.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatRoomEntity {
  private Long id;
  private LocalDateTime createdAt;
}
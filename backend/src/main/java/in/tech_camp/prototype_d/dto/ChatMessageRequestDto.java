package in.tech_camp.prototype_d.dto;

import lombok.Data;

@Data
public class ChatMessageRequestDto {
  private Long senderId;
  private String content;
}

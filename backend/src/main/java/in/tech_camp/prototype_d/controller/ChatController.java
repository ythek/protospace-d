package in.tech_camp.prototype_d.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import in.tech_camp.prototype_d.repository.ChatRepository;
import in.tech_camp.prototype_d.custom_user.CustomUserDetail;
import in.tech_camp.prototype_d.dto.ChatMessageRequestDto;
import in.tech_camp.prototype_d.dto.ChatRoomDetailDto;
import in.tech_camp.prototype_d.dto.ChatRoomListDto;
import in.tech_camp.prototype_d.entity.ChatMessageEntity;
import in.tech_camp.prototype_d.service.ChatService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {

  private final SimpMessagingTemplate messagingTemplate;
  private final ChatRepository chatRepository;
  private final ChatService chatService;


  // メッセージの送受信を処理(WebSocket通信)
  // フロントからは "/app/chat/{roomId}" 宛てに送信される
  @MessageMapping("/chat/{roomId}")
  @Transactional
  public void sendMessage(@DestinationVariable Long roomId, ChatMessageRequestDto request) {

    ChatMessageEntity message = new ChatMessageEntity();
    message.setChatRoomId(roomId);
    message.setSenderId(request.getSenderId());
    message.setContent(request.getContent());
    
    chatRepository.insertMessage(message);

    // フロント側にリアルタイム配信
    messagingTemplate.convertAndSend("/topic/rooms/" + roomId, message);
  }

  // 自分が参加しているチャットルーム一覧をJSONで返す
  @GetMapping("/api/chat/rooms")
  public List<ChatRoomListDto> getChatRoomList(@AuthenticationPrincipal CustomUserDetail currentUser) {
    if (currentUser == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "ログインが必要です");
    }

    

    Long myUserId = currentUser.getUser().getId();
    
    return chatService.getChatRoomsForUser(myUserId);
  }

  // 個別のチャットルーム情報をJSONで返す
  @GetMapping("/api/chat/{roomId}")
  public ChatRoomDetailDto getChatRoom(@PathVariable Long roomId, @AuthenticationPrincipal CustomUserDetail currentUser) {
    Long currentUserId = currentUser.getUser().getId(); 

    // 関係者以外立ち入り禁止
    boolean isMember = chatService.isMemberOfRoom(roomId, currentUserId);
    if (!isMember) {
        // 403Forbiddenを返す
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "アクセス権限がありません");
    }
    
    // 既読処理
    chatService.markAsRead(roomId, currentUserId);

    // 相手の名前を取得
    String counterpartName = chatService.getCounterpartName(roomId, currentUserId);
    
    // 過去のメッセージ履歴を取得
    List<ChatMessageEntity> messages = chatRepository.findByChatRoomId(roomId);

    return new ChatRoomDetailDto(roomId, counterpartName, messages);
  }
}
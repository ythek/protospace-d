package in.tech_camp.prototype_d.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.prototype_d.dto.ChatRoomListDto;
import in.tech_camp.prototype_d.entity.ChatMessageEntity;
import in.tech_camp.prototype_d.repository.ChatRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
  private final ChatRepository chatRepository;

  // 参加権限チェック
  public boolean isMemberOfRoom(Long roomId, Long userId) {
      return chatRepository.checkIsMember(roomId, userId) > 0;
  }

  // 相手の名前を取得
  public String getCounterpartName(Long roomId, Long currentUserId) {
      return chatRepository.findCounterpartName(roomId, currentUserId);
  }

  // ルーム一覧の取得
  public List<ChatRoomListDto> getChatRoomsForUser(Long myUserId) {
      return chatRepository.findChatRoomsForUser(myUserId);
  }

  // 過去のメッセージ履歴を取得
  public List<ChatMessageEntity> getMessages(Long roomId) {
      return chatRepository.findMessagesByRoomId(roomId);
  }

  // 未読・既読処理
  @Transactional
  public void markAsRead(Long roomId, Long userId) {
      // そのチャットルームの最新メッセージIDを取得
      Long latestMessageId = chatRepository.findLatestMessageId(roomId);
      
      // メッセージが1件でも存在する場合のみ、最終既読メッセージを更新
      if (latestMessageId != null) {
          chatRepository.updateLastReadMessageId(roomId, userId, latestMessageId);
      }
  }
}

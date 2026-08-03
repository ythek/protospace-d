package in.tech_camp.prototype_d.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.prototype_d.dto.ChatRoomListDto;
import in.tech_camp.prototype_d.entity.ChatMessageEntity;

public interface ChatRepository {
  //// メッセージ関連 ////
  // メッセージの保存
  @Insert("INSERT INTO messages (chat_room_id, sender_id, content) VALUES (#{chatRoomId}, #{senderId}, #{content})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertMessage(ChatMessageEntity message);

  // チャット履歴の取得（古い順）
  @Select("SELECT * FROM chat_messages WHERE chat_room_id = #{roomId} ORDER BY created_at ASC")
  List<ChatMessageEntity> findMessagesByRoomId(Long roomId);

  // ルーム内の最新メッセージIDを取得
  @Select("SELECT MAX(id) FROM chat_messages WHERE chat_room_id = #{roomId}")
  Long findLatestMessageId(Long roomId);


  //// ルームとメンバー関連 ////
  // メンバーかどうかの判定 (参加していれば1以上が返る)
  @Select("SELECT COUNT(*) FROM chat_room_members WHERE chat_room_id = #{roomId} AND user_id = #{userId}")
  int checkIsMember(@Param("roomId") Long roomId, @Param("userId") Long userId);

  // 相手の名前を取得する
  @Select("""
          SELECT users.username
          FROM chat_room_members
          JOIN users ON chat_room_members.user_id = users.id
          WHERE chat_room_members.chat_room_id = #{roomId} AND chat_room_members.user_id != #{currentUserId}
          LIMIT 1
      """)
  String findCounterpartName(@Param("roomId") Long roomId, @Param("userId") Long currentUserId);

  // 既読処理（最後に読んだメッセージIDを更新）
  @Update("""
          UPDATE chat_room_members
          SET last_read_message_id = #{messageId}
          WHERE chat_room_id = #{roomId} AND user_id = #{userId}
      """)
  void updateLastReadMessageId(@Param("roomId") Long roomId, @Param("userId") Long userId, @Param("messageId") Long messageId);

  // ユーザーが参加しているルーム一覧を取得
  @Select("""
          SELECT
              -- ルームID
              cr.id AS roomId,

              -- 相手のユーザーID
              (SELECT u.id
               FROM chat_room_members crm
               JOIN users u ON crm.user_id = u.id
               WHERE crm.chat_room_id = cr.id AND crm.user_id != #{myUserId}
               LIMIT 1) AS counterpartUserId,

              -- 相手のユーザー名
              (SELECT u.username
               FROM chat_room_members crm
               JOIN users u ON crm.user_id = u.id
               WHERE crm.chat_room_id = cr.id AND crm.user_id != #{myUserId}
               LIMIT 1) AS counterpartName,

              -- ルームの最新メッセージ（ない場合はnull）
              (SELECT content
               FROM chat_messages cm
               WHERE cm.chat_room_id = cr.id
               ORDER BY id DESC
               LIMIT 1) AS latestMessage

          FROM chat_rooms cr
          JOIN chat_room_members crm ON cr.id = crm.chat_room_id
          WHERE crm.user_id = #{myUserId}
      """)
  List<ChatRoomListDto> findChatRoomsForUser(Long myUserId);
}

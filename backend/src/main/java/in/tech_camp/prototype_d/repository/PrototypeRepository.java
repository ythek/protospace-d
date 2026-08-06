package in.tech_camp.prototype_d.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.prototype_d.dto.PrototypeListDto;
import in.tech_camp.prototype_d.entity.PrototypeEntity;

@Mapper
public interface PrototypeRepository {
  // プロトタイプ一覧
  @Select("SELECT p.id, p.user_id AS userId, p.title, p.catchcopy, p.image, u.username, p.created_at AS createdAt, " +
      "COUNT(l.id) AS likecount, " +
      "CASE WHEN COUNT (CASE WHEN l.user_id = #{userId} THEN 1 END) > 0 THEN 1  ELSE 0 END AS likecheck " +
      "FROM prototypes p " +
      "JOIN users u ON p.user_id = u.id " +
      "LEFT JOIN likes l ON p.id = l.prototype_id " +
      "GROUP BY p.id, u.username " +
      "ORDER BY p.id DESC")
  List<PrototypeListDto> findAll(@Param("userId") Long userId);


  @Select("SELECT id FROM prototypes")
  List<Long> findAllId();

  // プロトタイプ詳細
  @Select("SELECT p.id, p.user_id AS userId, p.title, p.catchcopy, p.concept, p.image, p.created_at AS createdAt " +
          "FROM prototypes p WHERE p.id = #{id}")
  PrototypeEntity findById( @Param("id") Long id, @Param("userId") Long userId);

  // 削除権限チェック用
  @Select("SELECT user_id FROM prototypes WHERE id = #{id}")
  Long findUserIdById(Long id);

  // プロトタイプ削除
  @Delete("DELETE FROM prototypes WHERE id = #{prototypeId}")
  void deletePrototype(Long prototypeId);

  // 編集
  @Update("UPDATE prototypes SET title = #{title}, catchcopy = #{catchcopy}, concept = #{concept}, image = #{image} WHERE id = #{id}")
    void update(PrototypeEntity prototype);

  // ユーザーごと取得
  @Select("""
            SELECT p.id, p.user_id AS userId, p.title, p.catchcopy, p.image, u.username
            FROM prototypes p
            JOIN users u ON p.user_id = u.id
            WHERE p.user_id = #{userId}
            ORDER BY p.id DESC
          """)
  List<PrototypeListDto> findByUserId(Long userId);

  // 新規投稿
  @Insert("INSERT INTO prototypes (title, catchcopy, concept, image, user_id) " +
        "VALUES (#{title}, #{catchcopy}, #{concept}, #{image}, #{userId})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(PrototypeEntity prototype);

  


}

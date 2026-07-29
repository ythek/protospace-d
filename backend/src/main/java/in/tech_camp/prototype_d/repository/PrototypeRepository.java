package in.tech_camp.prototype_d.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.prototype_d.dto.PrototypeListDto;
import in.tech_camp.prototype_d.entity.PrototypeEntity;

@Mapper
public interface PrototypeRepository {
  // プロトタイプ一覧
  @Select("SELECT p.id, p.user_id AS userId, p.title, p.catchcopy, p.image, u.username, " +
      "FROM prototypes p " +
      "JOIN users u ON p.user_id = u.id " +
      "ORDER BY p.id DESC")
  List<PrototypeListDto> findAll();

  // プロトタイプ詳細
  @Select("SELECT * FROM prototypes WHERE id = #{id}") 
  PrototypeEntity findById(Long id);

  // 削除権限チェック用
  @Select("SELECT user_id FROM prototypes WHERE id = #{id}")
  Long findUserIdById(Long id);

  // プロトタイプ削除
  @Delete("DELETE FROM prototypes WHERE id = #{prototypeId}")
  void deletePrototype(Long prototypeId);

  @Update("UPDATE prototypes SET title = #{title}, catchcopy = #{catchcopy}, concept = #{concept}, image = #{image} WHERE id = #{id}")
    void update(PrototypeEntity prototype);

  // ユーザーidで取得
  @Select("SELECT * FROM prototypes WHERE user_id = #{userId}")
  List<PrototypeEntity> findByUserId(Long userId);

  @Insert("INSERT INTO prototypes (title, catchcopy, concept, image, user_id) " +
        "VALUES (#{title}, #{catchcopy}, #{concept}, #{image}, #{userId})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(PrototypeEntity prototype);
}
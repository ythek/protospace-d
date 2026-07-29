package in.tech_camp.prototype_d.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.prototype_d.dto.PrototypeListDto;
import in.tech_camp.prototype_d.entity.PrototypeEntity;

@Mapper
public interface PrototypeRepository {
  // プロトタイプ一覧を取得
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

  // ユーザーidで取得
  @Select("SELECT * FROM prototypes WHERE user_id = #{userId}")
  List<PrototypeEntity> findByUserId(Long userId);
}
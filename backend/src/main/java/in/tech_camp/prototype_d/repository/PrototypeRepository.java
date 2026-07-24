package in.tech_camp.prototype_d.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.prototype_d.entity.PrototypeEntity;

@Mapper
public interface PrototypeRepository {
  // プロトタイプ一覧
  @Select("SELECT * FROM prototypes")
  List<PrototypeEntity> findAll();

  // プロトタイプ詳細
  @Select("SELECT * FROM prototypes WHERE id = #{id}") 
  PrototypeEntity findById(Long id);

  // 削除権限チェック用
  @Select("SELECT user_id FROM prototypes WHERE id = #{id}")
  Integer findUserIdById(Long id);

  // プロトタイプ削除
  @Delete("DELETE FROM prototypes WHERE id = #{id}")
  void deletePrototype(Long prototypeId);
}
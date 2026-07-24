package in.tech_camp.prototype_d.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.prototype_d.entity.PrototypeEntity;

@Mapper
public interface PrototypeRepository {
  @Select("SELECT * FROM prototypes")
  List<PrototypeEntity> findAll();

  @Select("SELECT * FROM prototypes WHERE id = #{id}") 
  PrototypeEntity findById(Integer id);

  @Update("UPDATE prototypes SET title = #{title}, catchcopy = #{catchcopy}, concept = #{concept} WHERE id = #{id}")
    void update(PrototypeEntity prototype);
}
package in.tech_camp.prototype_d.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import in.tech_camp.prototype_d.entity.PositionEntity;

@Mapper
public interface PositionRepository {
    @Select("SELECT id, position AS name FROM positions WHERE id = #{id}")
    PositionEntity findById(Integer id);
}
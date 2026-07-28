package in.tech_camp.prototype_d.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

import in.tech_camp.prototype_d.entity.PositionEntity;

@Mapper
public interface PositionRepository {

    @Select("SELECT id, position AS name FROM positions WHERE id = #{id}")
    PositionEntity findById(Long id);

    @Select("SELECT id FROM positions WHERE position = #{positionName}")
    Long findIdByName(String positionName);

    @Insert("INSERT INTO positions (position) VALUES (#{positionName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(PositionEntity position);
}
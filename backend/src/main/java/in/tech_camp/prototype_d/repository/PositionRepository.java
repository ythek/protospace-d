package in.tech_camp.prototype_d.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.prototype_d.entity.PositionEntity;

@Mapper
public interface PositionRepository {
    @Select("SELECT id FROM positions WHERE position = #{positionName}")
    Integer findIdByName(String positionName);

    @Select("SELECT position FROM positions WHERE id = #{id}")
    String findNameById(Integer id);

    @Insert("INSERT INTO positions (position) VALUES (#{positionName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(PositionEntity position);
}

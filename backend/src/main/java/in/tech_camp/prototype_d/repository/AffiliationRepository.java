package in.tech_camp.prototype_d.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import in.tech_camp.prototype_d.entity.AffiliationEntity;

@Mapper
public interface AffiliationRepository {
    @Select("SELECT id, affiliation AS name FROM affiliations WHERE id = #{id}")
    AffiliationEntity findById(Integer id);
}
package in.tech_camp.prototype_d.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.prototype_d.entity.AffiliationEntity;

@Mapper
public interface AffiliationRepository {
    @Select("SELECT id FROM affiliations WHERE affiliation = #{affiliationName}")
    Integer findIdByName(String affiliationName);

    @Insert("INSERT INTO affiliations (affiliation) VALUES (#{affiliationName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(AffiliationEntity affiliation);
}

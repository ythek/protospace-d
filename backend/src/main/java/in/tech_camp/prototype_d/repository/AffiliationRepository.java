package in.tech_camp.prototype_d.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.prototype_d.entity.AffiliationEntity;

@Mapper
public interface AffiliationRepository {
    @Select("SELECT id FROM affiliations WHERE affiliation = #{affiliationName}")
    Long findIdByName(String affiliationName);

    @Select("SELECT affiliation FROM affiliations WHERE id = #{id}")
    String findNameById(Long id);

    @Insert("INSERT INTO affiliations (affiliation) VALUES (#{affiliationName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(AffiliationEntity affiliation);
}

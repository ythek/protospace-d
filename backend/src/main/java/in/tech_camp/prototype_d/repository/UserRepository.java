package in.tech_camp.prototype_d.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.prototype_d.entity.UserEntity;

@Mapper
public interface UserRepository {
  @Insert("INSERT INTO users (username, email, password, profile, affiliation_id, position_id) VALUES (#{username}, #{email}, #{password}, #{profile}, #{affiliationId}, #{positionId})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(UserEntity user);

  @Select("SELECT EXISTS(SELECT 1 FROM users WHERE email = #{email})")
  boolean existsByEmail(String email);

  @Select("SELECT * FROM users WHERE email = #{email}")
  UserEntity findByEmail(String email);

  @Select("SELECT * FROM users")
  List<UserEntity> findAll();
}
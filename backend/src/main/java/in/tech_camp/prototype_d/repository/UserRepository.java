package in.tech_camp.prototype_d.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.prototype_d.entity.UserEntity;

@Mapper
public interface UserRepository {

    // ユーザー新規登録
    @Insert("INSERT INTO users (email, password, username, profile, affiliation_id, position_id) " +
            "VALUES (#{email}, #{password}, #{username}, #{profile}, #{affiliation.id}, #{position.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(UserEntity user);

    // ID指定でユーザーを取得（所属・役職情報も結合取得）
    @Select("SELECT id, email, password, username, profile, affiliation_id, position_id FROM users WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "email", column = "email"),
        @Result(property = "password", column = "password"),
        @Result(property = "username", column = "username"),
        @Result(property = "profile", column = "profile"),
        @Result(property = "affiliation", column = "affiliation_id",
                one = @One(select = "in.tech_camp.prototype_d.repository.AffiliationRepository.findById")),
        @Result(property = "position", column = "position_id",
                one = @One(select = "in.tech_camp.prototype_d.repository.PositionRepository.findById"))
    })
    UserEntity findById(Integer id);

    // メールアドレス指定でユーザーを取得（ログイン用）
    @Select("SELECT id, email, password, username, profile, affiliation_id, position_id FROM users WHERE email = #{email}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "email", column = "email"),
        @Result(property = "password", column = "password"),
        @Result(property = "username", column = "username"),
        @Result(property = "profile", column = "profile"),
        @Result(property = "affiliation", column = "affiliation_id",
                one = @One(select = "in.tech_camp.prototype_d.repository.AffiliationRepository.findById")),
        @Result(property = "position", column = "position_id",
                one = @One(select = "in.tech_camp.prototype_d.repository.PositionRepository.findById"))
    })
    UserEntity findByEmail(String email);
}
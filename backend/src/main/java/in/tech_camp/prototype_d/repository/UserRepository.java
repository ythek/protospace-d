package in.tech_camp.prototype_d.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;

import java.util.List;
import org.apache.ibatis.annotations.Select;
import in.tech_camp.prototype_d.entity.UserEntity;

@Mapper
public interface UserRepository {

    // ユーザー新規登録
    // ⭕ #{affiliation.id} ➔ #{affiliationId}、#{position.id} ➔ #{positionId} に変更
    @Insert("INSERT INTO users (email, password, username, profile, affiliation_id, position_id) " +
            "VALUES (#{email}, #{password}, #{username}, #{profile}, #{affiliationId}, #{positionId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(UserEntity user);

    // ID指定でユーザーを取得（引数も Long に変更）
    @Select("SELECT id, email, password, username, profile, affiliation_id, position_id FROM users WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "email", column = "email"),
        @Result(property = "password", column = "password"),
        @Result(property = "username", column = "username"),
        @Result(property = "profile", column = "profile"),
        @Result(property = "affiliationId", column = "affiliation_id"),
        @Result(property = "positionId", column = "position_id")
    })
    UserEntity findById(Long id);

    // メールアドレス指定でユーザーを取得
    @Select("SELECT id, email, password, username, profile, affiliation_id, position_id FROM users WHERE email = #{email}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "email", column = "email"),
        @Result(property = "password", column = "password"),
        @Result(property = "username", column = "username"),
        @Result(property = "profile", column = "profile"),
        @Result(property = "affiliationId", column = "affiliation_id"),
        @Result(property = "positionId", column = "position_id")
    })
    UserEntity findByEmail(String email);

    // メールアドレスの重複チェック用
    @Select("SELECT EXISTS(SELECT 1 FROM users WHERE email = #{email})")
    boolean existsByEmail(String email);

    // ユーザー全件取得
    @Select("SELECT * FROM users")
    List<UserEntity> findAll();
}
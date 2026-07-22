package in.tech_camp.prototype_d.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.prototype_d.entity.CommentEntity;

@Mapper
public interface CommentRepository {

    // コメント投稿
    @Insert("INSERT INTO comments (comment, user_id, prototype_id) VALUES (#{text}, #{user.id}, #{prototype.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(CommentEntity comment);

    // プロトタイプIDに紐づくコメント一覧を取得（ユーザー情報も同時取得）
    @Select("SELECT id, comment AS text, user_id, prototype_id FROM comments WHERE prototype_id = #{prototypeId}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "text", column = "text"),
        @Result(property = "user", column = "user_id",
                one = @One(select = "in.tech_camp.prototype_d.repository.UserRepository.findById"))
    })
    List<CommentEntity> findByPrototypeId(Integer prototypeId);
}
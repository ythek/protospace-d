package in.tech_camp.prototype_d.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.prototype_d.dto.CommentDto;
import in.tech_camp.prototype_d.entity.CommentEntity;

@Mapper
public interface CommentRepository {

    // コメントの保存（Entityの内容をDBに挿入）
    @Insert("INSERT INTO comments (comment, user_id, prototype_id) VALUES (#{comment}, #{user.id}, #{prototype.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(CommentEntity comment);

    // プロトタイプIDに紐づくコメント一覧を取得（ユーザー情報も同時に取得）
    @Select("SELECT id, comment, user_id FROM comments WHERE prototype_id = #{prototypeId} ORDER BY id DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "comment", column = "comment"),
        @Result(property = "user", column = "user_id",
                one = @One(select = "in.tech_camp.prototype_d.repository.UserRepository.findById"))
    })
    List<CommentEntity> findByPrototypeId(Long prototypeId);

    // DTOを使ってユーザー名入りのコメント一覧を取得したい場合
    @Select("""
            SELECT c.id, c.comment, c.user_id AS userId, u.username
            FROM comments c
            JOIN users u ON c.user_id = u.id
            WHERE c.prototype_id = #{prototypeId}
            ORDER BY c.id DESC
            """)
    List<CommentDto> findDtoByPrototypeId(Long prototypeId);
}
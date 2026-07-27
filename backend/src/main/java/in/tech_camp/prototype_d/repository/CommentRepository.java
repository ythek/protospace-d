package in.tech_camp.prototype_d.repository;

import in.tech_camp.prototype_d.dto.CommentDto;
import in.tech_camp.prototype_d.entity.CommentEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentRepository {

    // コメントの保存
    @Insert("INSERT INTO comments (comment, user_id, prototype_id) VALUES (#{comment}, #{userId}, #{prototypeId})")
    void save(CommentEntity commentEntity);

    // 特定のプロトタイプに紐づくコメント一覧（ユーザー名含む）を取得
    @Select("""
            SELECT c.id, c.comment, c.user_id AS userId, u.username
            FROM comments c
            JOIN users u ON c.user_id = u.id
            WHERE c.prototype_id = #{prototypeId}
            ORDER BY c.id DESC
            """)
    List<CommentDto> findByPrototypeId(Long prototypeId);
}
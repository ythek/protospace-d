package in.tech_camp.prototype_d.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.prototype_d.dto.PrototypeListDto;

@Mapper
public interface LikeRepository {
  //いいね追加
  @Insert("INSERT INTO likes (user_id, prototype_id)" +
          "VALUES (#{userId}, #{prototypeId})")
  void insertLikes(@Param("userId") Long userId,
                   @Param("prototypeId") Long prototypeId);

  //いいね削除
  @Delete("DELETE FROM likes WHERE user_id = #{userId} AND prototype_id = #{prototypeId}")
  void deleteLikes(@Param("userId") Long userId,
                   @Param("prototypeId") Long prototypeId);
  //いいねされているか判定 
  @Select("SELECT COUNT(*) FROM likes WHERE user_id = #{userId} AND prototype_id = #{prototypeId}")
   boolean existLikes(@Param("userId") Long userId,
                      @Param("prototypeId") Long prototypeId);

  //いいね総数取得
  @Select("SELECT COUNT(*) FROM likes WHERE prototype_id = #{prototypeId}" ) //自分がいいね何回されたかを知りたいためUserIdは不要
  long countAllLikes(Long prototypeId);

  //いいねが多い順
   
@Select("""
         SELECT p.id,
               p.user_id AS userId,
               p.title,
               p.catchcopy,
               p.image,
               u.username,
               p.created_at AS createdAt,
               COUNT(l.id) AS likecount,
               BOOL_OR(l.user_id = #{userId, jdbcType=BIGINT}) AS likecheck
        FROM prototypes p
        JOIN users u ON p.user_id = u.id
        LEFT JOIN likes l ON p.id = l.prototype_id
        GROUP BY p.id, u.username
        ORDER BY likecount DESC
        """)
  List<PrototypeListDto> orderByLikes (Long userId);
}

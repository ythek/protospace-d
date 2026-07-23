package in.tech_camp.prototype_d.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.prototype_d.entity.PrototypeEntity;

// @Mapper
// public interface PrototypeRepository {

//     // プロトタイプ全件取得（作成者ユーザー情報含む）
//     @Select("SELECT id, title, catchcopy, concept, image, user_id FROM prototypes ORDER BY id DESC")
//     @Results({
//         @Result(property = "id", column = "id"),
//         @Result(property = "title", column = "title"),
//         @Result(property = "catchcopy", column = "catchcopy"),
//         @Result(property = "concept", column = "concept"),
//         @Result(property = "image", column = "image"),
//         @Result(property = "user", column = "user_id",
//                 one = @One(select = "in.tech_camp.prototype_d.repository.UserRepository.findById"))
//     })
//     List<PrototypeEntity> findAll();

//     // ID指定で1件取得
//     @Select("SELECT id, title, catchcopy, concept, image, user_id FROM prototypes WHERE id = #{id}")
//     @Results({
//         @Result(property = "id", column = "id"),
//         @Result(property = "title", column = "title"),
//         @Result(property = "catchcopy", column = "catchcopy"),
//         @Result(property = "concept", column = "concept"),
//         @Result(property = "image", column = "image"),
//         @Result(property = "user", column = "user_id",
//                 one = @One(select = "in.tech_camp.prototype_d.repository.UserRepository.findById"))
//     })
//     PrototypeEntity findById(Integer id);

//     // 新規投稿
//     @Insert("INSERT INTO prototypes (title, catchcopy, concept, image, user_id) " +
//             "VALUES (#{title}, #{catchcopy}, #{concept}, #{image}, #{user.id})")
//     @Options(useGeneratedKeys = true, keyProperty = "id")
//     void insert(PrototypeEntity prototype);

//     // 更新
//     @Update("UPDATE prototypes SET title = #{title}, catchcopy = #{catchcopy}, concept = #{concept}, image = #{image} WHERE id = #{id}")
//     void update(PrototypeEntity prototype);

//     // 削除
//     @Delete("DELETE FROM prototypes WHERE id = #{id}")
//     void delete(Integer id);
// }

@Mapper
public interface PrototypeRepository {

    // プロトタイプ全件取得（作成者ユーザー情報含む）
    @Select("SELECT id, title, catchcopy, concept, image, user_id FROM prototypes ORDER BY id DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "title", column = "title"),
        @Result(property = "catchcopy", column = "catchcopy"),
        @Result(property = "concept", column = "concept"),
        @Result(property = "image", column = "image"),
        @Result(property = "user", column = "user_id",
                one = @One(select = "in.tech_camp.prototype_d.repository.UserRepository.findById"))
    })
    List<PrototypeEntity> findAll();

    // ID指定で1件取得
    @Select("SELECT id, title, catchcopy, concept, image, user_id FROM prototypes WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "title", column = "title"),
        @Result(property = "catchcopy", column = "catchcopy"),
        @Result(property = "concept", column = "concept"),
        @Result(property = "image", column = "image"),
        @Result(property = "user", column = "user_id",
                one = @One(select = "in.tech_camp.prototype_d.repository.UserRepository.findById"))
    })
    PrototypeEntity findById(Integer id);

    // 新規投稿
    @Insert("INSERT INTO prototypes (title, catchcopy, concept, image, user_id) " +
            "VALUES (#{title}, #{catchcopy}, #{concept}, #{image}, #{user.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(PrototypeEntity prototype);

    // 更新
    @Update("UPDATE prototypes SET title = #{title}, catchcopy = #{catchcopy}, concept = #{concept}, image = #{image} WHERE id = #{id}")
    void update(PrototypeEntity prototype);

    // 削除
    @Delete("DELETE FROM prototypes WHERE id = #{id}")
    void delete(Integer id);
}
package in.tech_camp.prototype_d.entity;

import lombok.Data;

@Data
public class CommentEntity {
    private Integer id;
    private String text; // DBの comment カラムに対応
    private UserEntity user;
    private PrototypeEntity prototype;
}

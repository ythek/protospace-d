package in.tech_camp.prototype_d.entity;

import lombok.Data;

@Data
public class CommentEntity {
    private Long id;          // Integer -> Long
    private Long userId;      // 外部キー用
    private Long prototypeId; // 外部キー用
    private String comment;
    private UserEntity user;
    private PrototypeEntity prototype;
}
package in.tech_camp.prototype_d.entity;

import lombok.Data;

@Data
public class PrototypeEntity {
    private Long id;          // Long に変更済み
    private Long userId;      // 外部キー用
    private UserEntity user;
    private String title;
    private String catchcopy;
    private String concept;
    private String image;
}
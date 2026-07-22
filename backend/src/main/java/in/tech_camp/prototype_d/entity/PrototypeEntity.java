package in.tech_camp.prototype_d.entity;

import lombok.Data;

@Data
public class PrototypeEntity {
    private Integer id;
    private String title;
    private String catchcopy;
    private String concept;
    private String image;
    private UserEntity user;
}
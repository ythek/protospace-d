package in.tech_camp.prototype_d.entity;

import lombok.Data;

@Data
public class UserEntity {
    private Integer id;
    private String email;
    private String password;
    private String username;
    private String profile;
    private AffiliationEntity affiliation;
    private PositionEntity position;
}

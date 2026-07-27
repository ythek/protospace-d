package in.tech_camp.prototype_d.custom_user;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import in.tech_camp.prototype_d.entity.UserEntity;
import lombok.Getter;

@Getter
public class CustomUserDetail implements UserDetails {

    private final UserEntity user;

    public CustomUserDetail(UserEntity user) {
        this.user = user;
    }

    // 便利なヘルパーメソッド
    public Integer getId() {
        return user != null ? user.getId() : null;
    }

    public String getEmail() {
        return user != null ? user.getEmail() : null;
    }

    // UserDetails インターフェースの実装メソッド
    @Override
    public String getUsername() {
        return user != null ? user.getEmail() : null;
    }

    @Override
    public String getPassword() {
        return user != null ? user.getPassword() : null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
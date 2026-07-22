package in.tech_camp.prototype_d.custom_user;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import in.tech_camp.prototype_d.entity.UserEntity;

public class CustomUserDetail implements UserDetails {

    private final UserEntity user;

    public CustomUserDetail(UserEntity user) {
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }

    // --- 以下、UserDetails インターフェースの必須メソッド ---

    @Override
    public String getUsername() {
        // UserEntity の email または名前などをユーザー名として返します
        return user != null ? user.getEmail() : null; 
    }

    @Override
    public String getPassword() {
        return user != null ? user.getPassword() : null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 権限管理（ロール）を使わない場合は空のリストを返します
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // アカウントの期限切れ判定（常に有効）
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // アカウントのロック判定（常に有効）
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 資格情報の期限切れ判定（常に有効）
    }

    @Override
    public boolean isEnabled() {
        return true; // アカウントの有効判定（常に有効）
    }
}
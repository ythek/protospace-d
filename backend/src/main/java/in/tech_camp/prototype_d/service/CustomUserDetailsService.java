package in.tech_camp.prototype_d.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.tech_camp.prototype_d.entity.UserEntity;
import in.tech_camp.prototype_d.repository.UserRepository;
import in.tech_camp.prototype_d.custom_user.CustomUserDetail;

@Service 
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // メールアドレスで DB から検索
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        // 上で定義した CustomUserDetail に包んで返す
        return new CustomUserDetail(user);
    }
}

package chnu.edu.lab1khrystafulova.user;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    // при старті записуємо 3 користувачів у MongoDB (паролі шифруються BCrypt)
    @PostConstruct
    void init() {
        repository.save(User.builder()
                .id("1")
                .firstName("Oleksandra")
                .lastName("Khrystafulova")
                .email("oleksandra@mail.com")
                .password(passwordEncoder.encode("admin"))
                .enabled(true)
                .accountLocked(false)
                .roles(List.of(Role.ADMIN))
                .build());

        repository.save(User.builder()
                .id("2")
                .firstName("Tom")
                .lastName("Thompson")
                .email("tom@mail.com")
                .password(passwordEncoder.encode("user"))
                .enabled(true)
                .accountLocked(false)
                .roles(List.of(Role.USER))
                .build());

        repository.save(User.builder()
                .id("3")
                .firstName("Peter")
                .lastName("Parker")
                .email("peter@mail.com")
                .password(passwordEncoder.encode("superadmin"))
                .enabled(true)
                .accountLocked(false)
                .roles(List.of(Role.SUPERADMIN))
                .build());
    }

    // Spring Security шукає користувача за "username",  це email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
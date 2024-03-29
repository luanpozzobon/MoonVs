package luan.moonvs.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collection;
import java.util.UUID;

@NoArgsConstructor
@Data

@Entity(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_user")
    private UUID idUser;

    @Column(name = "username",
            unique = true,
            nullable = false)
    private String username;

    @Column(name = "email",
            unique = true,
            nullable = false)
    private String email;

    @Column(name = "password_hash",
            nullable = false)
    private String password;

    @Column(name = "birth_date",
            nullable = false)
    private LocalDate birthDate;

    @CreationTimestamp
    @Column(name = "created_at",
            nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user",
              cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Profile profile;

    public User(User user) {
        this.idUser = user.getIdUser();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.birthDate = user.getBirthDate();
        this.createdAt = user.getCreatedAt();
        this.profile = user.getProfile();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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

    public boolean isOfLegalAge() {
        final int LEGAL_AGE = 18;

        return Period.between(this.birthDate, LocalDate.now()).getYears() > LEGAL_AGE;
    }
}
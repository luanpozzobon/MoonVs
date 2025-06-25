package lpz.moonvs.infra.auth.entity;

import lpz.utils.dao.annotations.Field;
import lpz.utils.dao.annotations.Table;

import java.util.UUID;

@Table(schema = "mvs", name = "user")
public class UserEntity {
    @Field(insertable = false, updatable = false, primaryKey = true)
    private UUID id;

    @Field(uniqueKey = true, nullable = false, length = 255)
    private String username;

    @Field(uniqueKey = true, nullable = false, length = 255)
    private String email;

    @Field(nullable = false)
    private String password;

    public UserEntity() { }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

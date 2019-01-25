package reservation.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author vvicario
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long userId;

    @NotNull
    private String fullName;

    @NotNull
    @Email
    private String email;

    protected User(){

    }

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

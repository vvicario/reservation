package reservation.services;


import reservation.domain.User;
import org.springframework.stereotype.Component;

/**
 * @author vvicario
 */
@Component
public interface UserService {

    User saveUser(User user);

    User findUserByEmail(String email);

}

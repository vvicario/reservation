package reservation.services;

import org.springframework.beans.factory.annotation.Autowired;
import reservation.domain.User;
import reservation.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * @author vvicario
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}

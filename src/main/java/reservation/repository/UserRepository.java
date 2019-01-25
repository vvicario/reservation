package reservation.repository;

import reservation.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vvicario
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);

}
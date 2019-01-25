package reservation.repository;

import org.springframework.data.repository.query.Param;
import reservation.domain.Reservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author vvicario
 */
@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    Integer checkForDateRangeOverlap(@Param("arrivalDate") LocalDate arrivalDate, @Param("departureDate") LocalDate departureDate);

    Integer checkForDateRangeOverlapUpdate(@Param("arrivalDate") LocalDate arrivalDate, @Param("departureDate") LocalDate departureDate, @Param("identifier") String identifier);

    List<Reservation> findAllByArrivalDateGreaterThanEqualAndDepartureDateLessThanEqual(LocalDate arrivalDate, LocalDate departureDate);

    Reservation findByIdentifier(String identifier);
}

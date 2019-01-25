package reservation.services;

import reservation.domain.Reservation;
import javassist.NotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author vvicario
 */
@Component
public interface ReservationService {

    CompletableFuture<Reservation> create(Reservation reservation) throws InterruptedException;

    CompletableFuture<List<LocalDate>> findAvailableDatesByRangeDates(String arrivalDate, String endDate);

    CompletableFuture<Reservation> update(String identifier, Reservation reservation) throws NotFoundException, InterruptedException;

    void delete(String identifier) throws NotFoundException;

    CompletableFuture<Reservation> findReservationByIdentifier(String identifier) throws NotFoundException;
}

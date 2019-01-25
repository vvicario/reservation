package reservation.services;

import reservation.domain.Reservation;
import reservation.dto.ReservationDTO;
import javassist.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author vvicario
 */
@Component
public interface ReservationService {

    CompletableFuture<Reservation> create(ReservationDTO reservationDTO) throws InterruptedException;

    CompletableFuture<List<LocalDate>> findAvailableDatesByRangeDates(LocalDate arrivalDate, LocalDate endDate);

    CompletableFuture<Reservation> update(String identifier, ReservationDTO reservationDTO) throws NotFoundException, InterruptedException;

    void delete(String identifier) throws NotFoundException;

}

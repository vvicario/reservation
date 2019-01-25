package reservation.services;

import org.apache.commons.lang.StringUtils;
import reservation.domain.Reservation;
import reservation.repository.ReservationRepository;
import reservation.util.Utils;
import reservation.validators.ReservationValidator;
import javassist.NotFoundException;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author vvicario
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationValidator validator;

    @Autowired
    private ReservationRepository reservationRepository;

    private static final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);

    @Transactional
    @Override
    @Async
    public CompletableFuture<Reservation> create(@Valid Reservation reservation) throws InterruptedException {

        reservation.setIdentifier(RandomStringUtils.random(8, true, true));
        try {
            if (!isOverlapping(reservation)) {
                reservationRepository.save(reservation);
            } else {
                throwConcurrentModificationException();
            }
        } catch (OptimisticLockException e) {
            saveAfterLock(reservation);
        }
        return CompletableFuture.completedFuture(reservation);
    }

    @Override
    @Transactional
    public CompletableFuture<Reservation> update(String identifier, @Valid Reservation reservation) throws NotFoundException, InterruptedException {
        validator.validate(reservation);
        Reservation existentReservation = reservationRepository.findByIdentifier(identifier);
        if(existentReservation == null) {
            throwNotFoundException();
        }
        try {
            if (!isOverlappingUpdate(reservation)) {
                reservation.setId(existentReservation.getId());
                reservationRepository.save(reservation);
            } else {
                throwConcurrentModificationException();
            }
        } catch (OptimisticLockException e) {
            saveAfterLock(reservation);
        }
        return CompletableFuture.completedFuture(reservation);
    }

    @Async
    @Override
    public CompletableFuture<List<LocalDate>> findAvailableDatesByRangeDates(String fromDate, String toDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate from = StringUtils.isEmpty(fromDate) ? LocalDate.now() : LocalDate.parse(fromDate, formatter);
        LocalDate to = StringUtils.isEmpty(toDate) ? LocalDate.now().plusMonths(1) : LocalDate.parse(toDate, formatter);
        validator.validateDateRange(from, to);
        List<LocalDate> availableDates = Utils.getDatesBetween(from, to);
        List<Reservation> reservations = reservationRepository.
                findAllByArrivalDateGreaterThanEqualAndDepartureDateLessThanEqual(from, to);
        List<LocalDate> existentReservations = new ArrayList<>();
        if(!CollectionUtils.isEmpty(reservations)) {
            reservations.forEach(reservation ->
                    // minus 1 becasuse the departure day will be free after 12
                existentReservations.addAll(Utils.getDatesBetween(reservation.getArrivalDate(), reservation.getDepartureDate().minusDays(1)))
            );
        }
        availableDates.removeAll(existentReservations);
        return CompletableFuture.completedFuture(availableDates);
    }

    @Async
    @Override
    public void delete(String identifier) throws NotFoundException {
        Reservation reservation = reservationRepository.findByIdentifier(identifier);
        if (reservation == null) {
            throwNotFoundException();
        }
        reservationRepository.delete(reservation);
    }

    @Async
    @Override
    public CompletableFuture<Reservation>  findReservationByIdentifier(String identifier) throws NotFoundException {
        Reservation reservation = reservationRepository.findByIdentifier(identifier);
        if(reservation == null) {
            throwNotFoundException();
        }
        return CompletableFuture.completedFuture(reservationRepository.findByIdentifier(identifier));
    }

    private boolean isOverlapping(Reservation reservation) {
        return reservationRepository.checkForDateRangeOverlap(
                reservation.getArrivalDate(),
                reservation.getDepartureDate()) != 0;
    }

    private boolean isOverlappingUpdate(Reservation reservation) {
        return reservationRepository.checkForDateRangeOverlapUpdate(
                reservation.getArrivalDate(),
                reservation.getDepartureDate(), reservation.getIdentifier()) != 0;
    }

    private void throwConcurrentModificationException() {
        throw new ConcurrentModificationException("Already exists a reservation for the specified range of dates.");
    }

    private void throwNotFoundException() throws NotFoundException {
        throw new NotFoundException("The specified reservation number does not exist.");
    }

    private void saveAfterLock(Reservation reservation) throws InterruptedException {
        // wait for 1 sec before try again
        log.error("Error saving reservation, waiting to retry.");
        Thread.sleep(1000);
        if (!isOverlapping(reservation)) {
            reservationRepository.save(reservation);
        } else {
            throwConcurrentModificationException();
        }
    }
}

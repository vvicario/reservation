package reservation.services;

import reservation.domain.Reservation;
import reservation.domain.User;
import reservation.dto.ReservationDTO;
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

    @Autowired
    private UserService userService;

    private static final Integer CANT_DEFAULT_MONTH = 1;

    private static final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);

    @Async
    @Transactional
    @Override
    public CompletableFuture<Reservation> create(@Valid ReservationDTO reservationDTO) throws InterruptedException {
        User user = getUser(reservationDTO);
        Reservation reservation = new Reservation(
                reservationDTO.getArrivalDate(),
                reservationDTO.getDepartureDate(),
                user,
                RandomStringUtils.random(8, true, true));
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

    @Async
    @Override
    public CompletableFuture<Reservation> update(String identifier, @Valid ReservationDTO reservationDTO) throws NotFoundException, InterruptedException {
        validator.validate(reservationDTO);
        Reservation reservation = reservationRepository.findByIdentifier(identifier);
        if(reservation == null) {
            throwNotFoundException();
        }
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

    @Async
    @Override
    public CompletableFuture<List<LocalDate>> findAvailableDatesByRangeDates(LocalDate fromDate, LocalDate toDate){
        validator.validateDateRange(fromDate, toDate);
        if(toDate == null) toDate =  LocalDate.now().plusMonths(CANT_DEFAULT_MONTH);
        List<LocalDate> availableDates = Utils.getDatesBetween(fromDate, toDate);
        List<Reservation> reservations = reservationRepository.
                findAllByArrivalDateGreaterThanEqualAndDepartureDateLessThanEqual(fromDate, toDate);
        List<LocalDate> existentReservations = new ArrayList<>();
        if(!CollectionUtils.isEmpty(reservations)) {
            reservations.forEach(reservation ->
                existentReservations.addAll(Utils.getDatesBetween(reservation.getArrivalDate(), reservation.getDepartureDate()))
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

    private boolean isOverlapping(Reservation reservation) {
        return reservationRepository.checkForDateRangeOverlap(
                reservation.getArrivalDate(),
                reservation.getDepartureDate()) != 0;
    }

    private User getUser(ReservationDTO reservationDTO) {
        User user = userService.findUserByEmail(reservationDTO.getEmail());
        if (user == null) {
            user = new User(reservationDTO.getFullName(), reservationDTO.getEmail());
            userService.saveUser(user);
        }
        return user;
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

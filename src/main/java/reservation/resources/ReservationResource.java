package reservation.resources;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import reservation.domain.Reservation;
import reservation.services.ReservationService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reservation.validators.ReservationValidator;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author vvicario
 */
@RestController
@RequestMapping("/reservation")
public class ReservationResource {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationValidator validator;

    /**
     * Create a reservation for the specified information
     * @param reservation
     * @param ucb
     * @return
     * @throws Exception
     */
    @PostMapping
    public ResponseEntity create(@RequestBody @Valid Reservation reservation, UriComponentsBuilder ucb) throws Exception {
        validator.validate(reservation);
        reservation = reservationService.create(reservation).get();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(reservation.getIdentifier()).toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Get reservation availability from today to one month or for the specified range of dates
     * @param from
     * @param to
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/available")
    public ResponseEntity<List<LocalDate>> findAvailableReservationDatesByRangeDates(@RequestParam(required=false) @DateTimeFormat(pattern = "yyyy-MM-dd") String from,
                                                                                     @RequestParam(required=false) @DateTimeFormat(pattern = "yyyy-MM-dd") String to) throws Exception {

        CompletableFuture<List<LocalDate>> availableDates = reservationService.findAvailableDatesByRangeDates(from, to);
        return new ResponseEntity<>(availableDates.get(), HttpStatus.OK);
    }

    /**
     * Return if exists reservation for the specified identifier or not found if it does not exists
     * @param identifier
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/{identifier}")
    public ResponseEntity<Reservation> findReservation(@PathVariable("identifier") String identifier) throws Exception {

        CompletableFuture<Reservation> reservation = reservationService.findReservationByIdentifier(identifier);
        return ResponseEntity.ok(reservation.get());
    }

    /**
     * Remove if exists reservation for the specified identifier
     * @param identifier
     * @return
     * @throws NotFoundException
     */
    @DeleteMapping(value = "/{identifier}")
    public ResponseEntity<String> delete(@PathVariable("identifier") String identifier) throws NotFoundException {
        reservationService.delete(identifier);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update with the provided information, reservation with the specified identifier
     * @param identifier
     * @param reservation
     * @return
     * @throws NotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @PutMapping(value = "/{identifier}")
    public ResponseEntity<Reservation> update(@PathVariable("identifier") String identifier, @RequestBody @Valid Reservation reservation) throws NotFoundException, InterruptedException, ExecutionException {
        return ResponseEntity.ok(reservationService.update(identifier, reservation).get());
    }
}

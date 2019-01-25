package reservation.resources;

import reservation.domain.Reservation;
import reservation.dto.ReservationDTO;
import reservation.dto.Response;
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

    @PostMapping
    public CompletableFuture<ResponseEntity<Response>> create(@RequestBody @Valid ReservationDTO reservationDTO, UriComponentsBuilder ucb) throws Exception {
        Response response = new Response();
        long start = System.currentTimeMillis();
        validator.validate(reservationDTO);
        return reservationService.create(reservationDTO)
                .thenApply(reservation -> {
                    response.setData(reservation);
                    response.setTimeMs(System.currentTimeMillis() - start);
                    response.setCompletingThread(Thread.currentThread().getName());
                    return ResponseEntity.ok().body(response);
                });
    }

    @GetMapping(value = "/available")
    public ResponseEntity<List<LocalDate>> findAvailableReservationDatesByRangeDates(@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                                                                                     @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) throws Exception {

        CompletableFuture<List<LocalDate>> availableDates = reservationService.findAvailableDatesByRangeDates(fromDate, toDate);
        return new ResponseEntity<>(availableDates.get(), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("identifier") String identifier) throws NotFoundException {
        reservationService.delete(identifier);
        return new ResponseEntity<>("Reservation was canceled", HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Reservation> update(@RequestParam("identifier") String identifier, @RequestBody ReservationDTO reservation) throws NotFoundException, InterruptedException, ExecutionException {
        return new ResponseEntity<>(reservationService.update(identifier, reservation).get(), HttpStatus.OK);
    }
}

package reservation.validators;

import reservation.dto.ReservationDTO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;

public class ReservationValidatorTest {

    private ReservationValidator validator;

    @Before
    public void setUp() {
        validator = new ReservationValidator();
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testValidateMinimumDaysBeforeToReserve() {
        LocalDate arrivalDate = LocalDate.now();
        LocalDate departureDate = LocalDate.now().plusDays(1);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(ReservationValidator.ERROR_MESSAGE_MINIMUM_DAYS);
        validator.validate(createReservationDTO(arrivalDate, departureDate));
    }

    @Test
    public void testValidateDepartureDateLessThan1Month() {
        LocalDate arrivalDate = LocalDate.of(2019,1,25);
        LocalDate departureDate = LocalDate.of(2019,2,24);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(ReservationValidator.ERROR_MESSAGE_MAXIMUM_DAYS_IN_ADVANCE);
        validator.validate(createReservationDTO(arrivalDate, departureDate));
    }

    @Test
    public void testValidateArrivalDateGreaterThanDepartureDate() {
        LocalDate arrivalDate = LocalDate.now().plusDays(3);
        LocalDate departureDate = LocalDate.now().plusDays(1);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(ReservationValidator.ERROR_MESSAGE_DEPARTURE_DATE);
        validator.validate(createReservationDTO(arrivalDate, departureDate));
    }

    @Test
    public void testValidateTotalDaysGreaterThanAllowed() {
        LocalDate arrivalDate = LocalDate.now().plusDays(3);
        LocalDate departureDate = LocalDate.now().plusDays(7);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(ReservationValidator.ERROR_MESSAGE_MAXIMUM_DAYS);
        validator.validate(createReservationDTO(arrivalDate, departureDate));
    }

    @Test
    public void testValidateSuccess() {
        LocalDate arrivalDate = LocalDate.now().plusDays(3);
        LocalDate departureDate = LocalDate.now().plusDays(5);
        validator.validate(createReservationDTO(arrivalDate, departureDate));
    }

    @Test
    public void testValidateInvalidFromDate() {
        LocalDate arrivalDate = LocalDate.now().minusDays(3);
        LocalDate departureDate = LocalDate.now().plusDays(5);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(ReservationValidator.ERROR_MESSAGE_FROM_DATE);
        validator.validateDateRange(arrivalDate, departureDate);
    }

    @Test
    public void testValidateInvalidToDate() {
        LocalDate arrivalDate = LocalDate.now().plusDays(3);
        LocalDate departureDate = LocalDate.now().plusDays(35);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(String.format(ReservationValidator.ERROR_MESSAGE_TO_DATE, LocalDate.now().plusMonths(1)));
        validator.validateDateRange(arrivalDate, departureDate);
    }

    private ReservationDTO createReservationDTO(LocalDate arrivalDate, LocalDate departureDate){
        ReservationDTO reservation = new ReservationDTO();
        reservation.setArrivalDate(arrivalDate);
        reservation.setDepartureDate(departureDate);
        return reservation;
    }
}

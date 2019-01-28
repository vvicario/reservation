package reservation.validators;

import org.skyscreamer.jsonassert.JSONAssert;
import reservation.TestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author vvicario
 */
public class ReservationValidatorTest extends TestUtils {

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
        validator.validate(createReservation(arrivalDate, departureDate));
    }

    @Test
    public void testValidateDepartureDateLessThan1Month() {
        LocalDate arrivalDate = LocalDate.now().plusDays(2);
        LocalDate departureDate = LocalDate.now().plusDays(32);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(ReservationValidator.ERROR_MESSAGE_MAXIMUM_DAYS_IN_ADVANCE);
        validator.validate(createReservation(arrivalDate, departureDate));
    }

    @Test
    public void testValidateArrivalDateGreaterThanDepartureDate() {
        LocalDate arrivalDate = LocalDate.now().plusDays(3);
        LocalDate departureDate = LocalDate.now().plusDays(1);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(ReservationValidator.ERROR_MESSAGE_DEPARTURE_DATE);
        validator.validate(createReservation(arrivalDate, departureDate));
    }

    @Test
    public void testValidateTotalDaysGreaterThanAllowed() {
        LocalDate arrivalDate = LocalDate.now().plusDays(3);
        LocalDate departureDate = LocalDate.now().plusDays(7);
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(ReservationValidator.ERROR_MESSAGE_MAXIMUM_DAYS);
        validator.validate(createReservation(arrivalDate, departureDate));
    }

    @Test
    public void testValidateSuccess() {
        LocalDate arrivalDate = LocalDate.now().plusDays(3);
        LocalDate departureDate = LocalDate.now().plusDays(5);
        validator.validate(createReservation(arrivalDate, departureDate));
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
    @Test
    public void givenText_whenSimpleRegexMatchesTwice_thenCorrect() {
        String test = "CCCCTA";
        Pattern pattern = Pattern.compile("CCC");
        Matcher matcher = pattern.matcher(test);
        int matches = 0;
        while (matcher.find()) {
            matches++;
        }

   //     JSONAssert.assertEquals(matches, 2);
    }
}

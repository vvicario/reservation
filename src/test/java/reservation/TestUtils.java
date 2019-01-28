package reservation;

import reservation.domain.Reservation;

import java.time.LocalDate;

/**
 * @author vvicario
 */
public abstract class TestUtils {

    protected static final String EMAIL = "vvicario@test.com";
    protected static final String FULL_NAME = "Veronica Vicario";

    protected Reservation createReservation(LocalDate arrivalDate, LocalDate departureDate){
        Reservation reservation = new Reservation();
        reservation.setArrivalDate(arrivalDate);
        reservation.setDepartureDate(departureDate);
        return reservation;
    }

    protected Reservation createReservation(LocalDate arrivalDate, LocalDate departureDate, String email, String fullName){
        Reservation reservation = new Reservation();
        reservation.setArrivalDate(arrivalDate);
        reservation.setDepartureDate(departureDate);
        reservation.setUserEmail(email);
        reservation.setUserFullName(fullName);
        return reservation;
    }

}

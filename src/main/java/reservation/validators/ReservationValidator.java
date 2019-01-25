package reservation.validators;

import reservation.dto.ReservationDTO;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;


/**
 * @author vvicario
 */
@Component
public class ReservationValidator {

    static final Integer MIN_RESERVATION_DATES = 1;

    static final Integer MAX_RESERVATION_DATES = 3;

    static final String ERROR_MESSAGE_MINIMUM_DAYS = "Arrival Date must be minimum 1 day ahead of arrival.";

    static final String ERROR_MESSAGE_MAXIMUM_DAYS_IN_ADVANCE = "Departure Date must be maximum 1 month in advance.";

    static final String ERROR_MESSAGE_DEPARTURE_DATE = "Departure Date must be greater than Arrival Date.";

    static final String ERROR_MESSAGE_MAXIMUM_DAYS = "The reservation can be reserved for max 3 days.";

    static final String ERROR_MESSAGE_FROM_DATE = "From date must be greater than today";

    static final String ERROR_MESSAGE_TO_DATE = "End date must be less than: %s";

    public void validate(Object obj) {

        ReservationDTO reservationDTO = (ReservationDTO) obj;

        // The reservation can be reserved minimum 1 day(s) ahead of arrival
        if (reservationDTO.getArrivalDate().isBefore(LocalDate.now().plusDays(MIN_RESERVATION_DATES))) {
            throwIllegalArgumentException(ERROR_MESSAGE_MINIMUM_DAYS);
        }

        // The reservation can be reserved MAX 1 month in advance
        if (reservationDTO.getDepartureDate().isAfter(LocalDate.now().plusMonths(1))) {
            throwIllegalArgumentException(ERROR_MESSAGE_MAXIMUM_DAYS_IN_ADVANCE);
        }

        // arrival date should be greater than departure date
        if (reservationDTO.getArrivalDate().isAfter(reservationDTO.getDepartureDate())) {
            throwIllegalArgumentException(ERROR_MESSAGE_DEPARTURE_DATE);
        }
        // reservation can be reserved for max 3 days
        else if (Duration.between(reservationDTO.getArrivalDate().atStartOfDay(), reservationDTO.getDepartureDate().atStartOfDay()).toDays()
                > MAX_RESERVATION_DATES) {
            throwIllegalArgumentException(ERROR_MESSAGE_MAXIMUM_DAYS);
        }
    }

    public void validateDateRange(LocalDate fromDate, LocalDate toDate) {
        if(fromDate.isBefore(LocalDate.now())){
            throwIllegalArgumentException(ERROR_MESSAGE_FROM_DATE);
        }
        LocalDate defaultEndDate = LocalDate.now().plusMonths(1);
        if(toDate.isAfter(defaultEndDate)) {
            throwIllegalArgumentException(String.format(ERROR_MESSAGE_TO_DATE, defaultEndDate.toString()));
        }
    }

    private void throwIllegalArgumentException(String message) {
        throw new IllegalArgumentException(message);
    }

}

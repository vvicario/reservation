package reservation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Future;
import java.time.LocalDate;

/**
 * @author vvicario
 */
@Component
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReservationDTO {

    @NotNull
    @Future(message = "Arrival date should be greater than today.")
    private LocalDate arrivalDate;

    @NotNull(message = "Departure date is required.")
    @Future(message = "Departure date should be greater than today.")
    private LocalDate departureDate;

    @NotNull(message = "Full name is required.")
    private String fullName;

    @NotNull(message = "Email is required.")
    @Email(message = "Email Address is in an invalid format.")
    private String email;

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

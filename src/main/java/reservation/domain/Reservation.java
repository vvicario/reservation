package reservation.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author vvicario
 */
@Entity
// EndA <= StartB or StartA >= EndB
@NamedQueries({
        @NamedQuery(name = "Reservation.checkForDateRangeOverlap",
                query = "select count(r.id) from Reservation r where r.departureDate >= :arrivalDate or " +
                        "r.arrivalDate <= :departureDate ",
                lockMode = LockModeType.WRITE),
        @NamedQuery(name = "Reservation.checkForDateRangeOverlapUpdate",
                query = "select count(r.id) from Reservation r where r.identifier != :identifier and (r.departureDate >= :arrivalDate or " +
                        "r.arrivalDate <= :departureDate) ",
                lockMode = LockModeType.WRITE)
})
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Email
    private String userEmail;

    @NotNull
    private String userFullName;

    @NotNull
    private LocalDate arrivalDate;

    @NotNull
    private LocalDate departureDate;

    private String identifier;

    @Version
    private Integer version;

    public Reservation() {

    }

    public Reservation(LocalDate arrivalDate, LocalDate departureDate, String userEmail, String userFullName, String identifier) {
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.userEmail = userEmail;
        this.userFullName = userFullName;
        this.identifier = identifier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }
}

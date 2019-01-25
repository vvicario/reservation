package reservation.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author vvicario
 */
@Entity
// EndA <= StartB or StartA >= EndB
@NamedQuery(name = "Reservation.checkForDateRangeOverlap",
        query = "select count(r.id) from Reservation r where r.departureDate >= :arrivalDate or " +
                "r.arrivalDate <= :departureDate ",
        lockMode = LockModeType.WRITE)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @NotNull
    private LocalDate arrivalDate;

    @NotNull
    private LocalDate departureDate;

    private String identifier;

    @Version
    private Integer version;

    protected Reservation() {

    }

    public Reservation(LocalDate arrivalDate, LocalDate departureDate, User user, String identifier) {
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.user = user;
        this.identifier = identifier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}

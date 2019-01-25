package reservation.resources;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import reservation.ReservationApplication;
import reservation.TestUtils;
import reservation.domain.Reservation;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReservationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationResourceIT extends TestUtils {


    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private HttpHeaders headers = new HttpHeaders();

    private static String location;

    @Test
    public void testReservation() {
        testCreateSuccessReservation();
        testGetReservation();
        testCreateReservationForExistentDays();
        testUpdateReservation();
        testDeleteReservation();
    }

    public void testCreateReservationWithInvalidParameters() {
        Reservation reservationDTO = createReservation(
                LocalDate.now().minusDays(2),
                LocalDate.now().plusDays(4),
                EMAIL,
                FULL_NAME);

        HttpEntity<Reservation> entity = new HttpEntity<>(reservationDTO, headers);
        ResponseEntity<String> badResponse = restTemplate.exchange(
                createURLWithPort("/reservation"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, badResponse.getStatusCode());
    }

    private String testCreateSuccessReservation() {
        Reservation reservationDTO = createReservation(
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(4),
                EMAIL,
                FULL_NAME);
        HttpEntity<Reservation> entity = new HttpEntity<>(reservationDTO, headers);
        // create reservation
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/reservation"),
                HttpMethod.POST, entity, String.class);

        location = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
        Assert.assertTrue(location.contains("/reservation/"));
        return location;

    }

    private void testGetReservation() {
        HttpEntity<Reservation> entity = new HttpEntity<>(headers);
        // get reservation
        ResponseEntity<Reservation> reservationResponse = restTemplate.exchange(
                location,
                HttpMethod.GET, entity,
                Reservation.class);
        Reservation reservation = reservationResponse.getBody();
        Assert.assertTrue(reservation.getArrivalDate().equals(LocalDate.now().plusDays(2)));
        Assert.assertTrue(reservation.getDepartureDate().equals(LocalDate.now().plusDays(4)));
    }

    private void testCreateReservationForExistentDays() {

        Reservation reservationDTO = createReservation(
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(4),
                EMAIL,
                FULL_NAME);
        HttpEntity<Reservation> entity = new HttpEntity<>(reservationDTO, headers);
        // create reservation for existent days
        ResponseEntity<String> responseFailed = restTemplate.exchange(
                createURLWithPort("/reservation"),
                HttpMethod.POST, entity, String.class);
        Assert.assertEquals(HttpStatus.CONFLICT, responseFailed.getStatusCode());
    }

    private void testUpdateReservation() {
        Reservation reservationDTO = createReservation(
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(4),
                EMAIL,
                FULL_NAME);
        HttpEntity<Reservation> entity = new HttpEntity<>(reservationDTO, headers);
        ResponseEntity<Reservation> response = restTemplate.exchange(
                location,
                HttpMethod.PUT, entity, Reservation.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private void testDeleteReservation() {
        HttpEntity<Reservation> entity = new HttpEntity<>(headers);
        ResponseEntity<Reservation> response = restTemplate.exchange(
                location,
                HttpMethod.DELETE, entity, Reservation.class);
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}

package reservation.services;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import reservation.ReservationApplication;
import reservation.TestUtils;
import reservation.domain.Reservation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author vvicario
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ReservationApplication.class}, loader = AnnotationConfigContextLoader.class)
public class ReservationServiceTest extends TestUtils {

    @Autowired
    private ReservationServiceImpl service;

    @Test
    public void testFindAvailableDates()
            throws InterruptedException, ExecutionException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        CompletableFuture<List<LocalDate>> future = service.findAvailableDatesByRangeDates(LocalDate.now().plusDays(2).format(formatter), LocalDate.now().plusDays(4).format(formatter));
        CompletableFuture<List<LocalDate>> future2 = service.findAvailableDatesByRangeDates(LocalDate.now().plusDays(3).format(formatter), LocalDate.now().plusDays(7).format(formatter));
        CompletableFuture<List<LocalDate>> future3 = service.findAvailableDatesByRangeDates(LocalDate.now().plusDays(4).format(formatter), LocalDate.now().plusDays(7).format(formatter));
        CompletableFuture<List<LocalDate>> future4 = service.findAvailableDatesByRangeDates(LocalDate.now().plusDays(5).format(formatter), null);
        // Wait until they are all done
        CompletableFuture.allOf(future, future2, future3, future4).join();
        Assert.assertTrue(future.get().size() == 3);
        Assert.assertTrue(future2.get().size() == 5);
        Assert.assertTrue(future3.get().size() == 4);
        Assert.assertFalse(future4.isCompletedExceptionally());
    }


    @Test
    public void testCreateReservation()
            throws InterruptedException, ExecutionException {
        Reservation reservationDTO = createReservation(
                LocalDate.now().plusDays(8),
                LocalDate.now().plusDays(10),
                EMAIL,
                FULL_NAME);
        Reservation reservationDTO2 = createReservation(
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(11),
                EMAIL,
                FULL_NAME);
        CompletableFuture<Reservation> future = service.create(reservationDTO);
        CompletableFuture<Reservation> future2 = service.create(reservationDTO2);
        CompletableFuture<Reservation> future3 = service.create(reservationDTO);
        Assert.assertTrue(StringUtils.isNotEmpty(future2.get().getIdentifier()));
        try{
            future.get();
        }catch (Exception e) {
            Assert.assertTrue(StringUtils.isNotEmpty(future3.get().getIdentifier()));
            Assert.assertTrue(e.getMessage().contains("Already exists a reservation for the specified range of dates."));
        }
        try{
            future3.get();
        }catch (Exception e) {
            Assert.assertTrue(StringUtils.isNotEmpty(future.get().getIdentifier()));
            Assert.assertTrue(e.getMessage().contains("Already exists a reservation for the specified range of dates."));
        }
    }


}

package reservation.util;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

/**
 * @author vvicario
 */
public class UtilsTest {

    @Test
    public void testGetDatesBetween(){
        LocalDate startDate = LocalDate.of(2019, 1, 27);
        LocalDate endDate = LocalDate.of(2019, 2, 5);
        List<LocalDate> dates = Utils.getDatesBetween(startDate, endDate);
        Assert.assertEquals(10, dates.size());
        Assert.assertFalse(dates.contains(LocalDate.of(2019, 1, 26)));
        Assert.assertTrue(dates.contains(LocalDate.of(2019, 1, 27)));
        Assert.assertTrue(dates.contains(LocalDate.of(2019, 1, 28)));
        Assert.assertTrue(dates.contains(LocalDate.of(2019, 1, 29)));
        Assert.assertTrue(dates.contains(LocalDate.of(2019, 1, 30)));
        Assert.assertTrue(dates.contains(LocalDate.of(2019, 1, 31)));
        Assert.assertTrue(dates.contains(LocalDate.of(2019, 2, 1)));
        Assert.assertTrue(dates.contains(LocalDate.of(2019, 2, 2)));
        Assert.assertTrue(dates.contains(LocalDate.of(2019, 2, 3)));
        Assert.assertTrue(dates.contains(LocalDate.of(2019, 2, 4)));
        Assert.assertTrue(dates.contains(LocalDate.of(2019, 2, 5)));
        Assert.assertFalse(dates.contains(LocalDate.of(2019, 2, 6)));
    }
}

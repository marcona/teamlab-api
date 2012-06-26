import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: marcona
 * Date: 26/06/12
 * Time: 22:30
 * To change this template use File | Settings | File Templates.
 */
public class TeamLabApiTest {

    @Test
    public void test_connection() throws Exception {
        TeamLabApi api = new TeamLabApi("http://XXXX", "XXX", "XXX");
        assertNotNull(api.getToken());

        String allEvents = api.getAllEvents();
        System.out.println("allEvents = " + allEvents);
    }
}

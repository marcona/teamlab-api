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
        TeamLabApi api = new TeamLabApi("http://XX", "XXX", "XXX");
        assertNotNull(api.getToken());

        String allEvents = api.getAllEvents();
        System.out.println("allEvents = " + allEvents);

        String event = api.getEvent("41970");
        System.out.println("event = " + event);

        String content="&lt;p&gt;Bonjour,&lt;/p&gt;\n" +
                "            &lt;p&gt;la version 2.22 du framework&amp;nbsp;est disponible sur le repository binaire codjo.&lt;br /&gt;\n" +
                "            &lt;br /&gt;\n" +
                "            Cordialement,&lt;/p&gt;\n" +
                "            &lt;p&gt;Codjo Team (du 8eme)&lt;/p&gt;";
        String result = api.postEvent("http://team.codjo.net", "[Test API REST]] Livraison du frameworks codjo 2.22","1",content);
        System.out.println("result = " + result);

    }
}

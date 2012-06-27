import org.junit.Test;

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
        TeamLabApi api = new TeamLabApi("http://XXX",
                                        "XXTeamLabAccount",
                                        "XXTeamLabPassword",
                                        new ProxyInformation("XXProxyHost", 80, "XXProxyUser", "XXProxyPassword")
        );
        assertNotNull(api.getToken());

        String allEvents = api.getAllEvents();
        System.out.println("allEvents = " + allEvents);

        String event = api.getEvent("41970");
        System.out.println("event = " + event);

        String content="Bonjour,\n" +
                "            la version 2.22 du framework est disponible sur le repository binaire codjo.\n" +
                "            \n" +
                "            Cordialement,\n" +
                "            Codjo Team (du 8eme)";
        
        //String result = api.postEvent("[Test API REST]] Livraison du frameworks codjo 2.22","1",content);
        //System.out.println("result = " + result);
    }
}

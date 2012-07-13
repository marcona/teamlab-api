import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Request;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

public class TeamLabApiTest extends JettyFixture {
    private static final String FAKE_TOKEN = "sdjhfskjdhkqy739459234";
    private String allEvents = "";


    @Before
    public void setup() throws Exception {
        super.doSetup();
    }


    @After
    public void tearDown() throws Exception {
        super.doTearDown();
    }


    @Override
    protected String[] getRoles() {
        return null;
    }


    @Test
    public void test_teamLabApi() throws IOException {
        TeamLabApi api = new TeamLabApi("http://localhost:8080", "admin", "admin", null);

        assertThat(api.getToken(), is(FAKE_TOKEN));
        assertThat(api.getAllEvents(), is(""));
        assertThat(api.getEvent("41970"), containsString("result"));

        String content = "Bonjour,\n" +
                         "            la version 2.22 du framework est disponible sur le repository binaire codjo.\n"
                         +
                         "            \n" +
                         "            Cordialement,\n" +
                         "            Codjo Team (du 8eme)";

        String result = api.postEvent("[Test API REST]] Livraison du frameworks codjo 2.22", "1", content);
        assertThat(result, is(api.getAllEvents()));
    }


    @Override
    protected void handleHttpRequest(String target, HttpServletRequest request, HttpServletResponse response) {
        try {
            if ("GET".equals(request.getMethod())) {
                if ("/api/1.0/event".equals(target)) {
                    response.getWriter().print(getAllEvents());
                }
                else if ("/api/1.0/event/41970".equals(target)) {
                    response.getWriter().print(getOneEvent());
                }
                response.getWriter().close();
            }
            if ("POST".equals(request.getMethod())) {
                if ("/api/1.0/authentication".equals(target)) {
                    response.getWriter().print(getAuthenticationResult());
                }
                else {
                    String authorization = request.getHeader("Authorization");
                    if (!FAKE_TOKEN.equals(authorization)) {
                        throw new IllegalArgumentException("Vous n'etes pas autorisé a utiliser Teamlab api");
                    }
                    if ("/api/1.0/event".equals(target)) {
                        allEvents = decode(request);
                        response.getWriter().print(allEvents);
                    }
                    else {
                        response.getWriter().print(decode(request));
                    }
                }
                response.getWriter().close();
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (IOException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        response.setContentType(APPLICATION_XML);
        ((Request)request).setHandled(true);
    }


    private String getOneEvent() {
        return "<result>\n"
               + "    <count>0</count>\n"
               + "    <startIndex>0</startIndex>\n"
               + "    <status>0</status>\n"
               + "    <statusCode>0</statusCode>\n"
               + "    <response>\n"
               + "        <id>10</id>\n"
               + "        <title>Sample news</title>\n"
               + "        <created>2012-07-13T13:31:03.8960895Z</created>\n"
               + "        <updated>2012-07-13T13:31:03.8960895Z</updated>\n"
               + "        <type>1</type>\n"
               + "        <createdBy>\n"
               + "            <id>6dc132c6-9efc-4b53-8011-ef7439455f87</id>\n"
               + "            <displayName>Mike Zanyatski</displayName>\n"
               + "            <title>Manager</title>\n"
               + "            <avatarSmall>url to small avatar</avatarSmall>\n"
               + "        </createdBy>\n"
               + "        <text>Text of feed</text>\n"
               + "        <poll>\n"
               + "            <pollType>0</pollType>\n"
               + "            <endDate>2012-07-13T13:31:03.8960895Z</endDate>\n"
               + "            <startDate>2012-07-13T13:31:03.8960895Z</startDate>\n"
               + "            <voted>false</voted>\n"
               + "            <votes>\n"
               + "                <id>133</id>\n"
               + "                <name>Variant 1</name>\n"
               + "                <votes>100</votes>\n"
               + "            </votes>\n"
               + "        </poll>\n"
               + "    </response>\n"
               + "</result>";
    }


    private String getAllEvents() {
        return allEvents;
    }


    private String getAuthenticationResult() {
        return "<result>"
               + "<count>0</count> "
               + "<startIndex>0</startIndex>"
               + "<status>0</status>"
               + "<statusCode>0</statusCode>"
               + "<response>"
               + "<token>" + FAKE_TOKEN + "</token>"
               + "<expires>2013-07-13T04:30:27.8782705Z</expires> "
               + "</response>"
               + "</result>";
    }
}

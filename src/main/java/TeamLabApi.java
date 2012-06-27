import bean.Result;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * Created with IntelliJ IDEA. User: marcona Date: 26/06/12 Time: 22:08 To change this template use File | Settings |
 * File Templates.
 */
public class TeamLabApi {
    private String host;
    private String token;
    private HttpClient httpClient;


    public TeamLabApi(String host, String apiUserName, String apiPassword) throws IOException {
        this(host, apiUserName, apiPassword, null);
    }


    public TeamLabApi(String host, String apiUserName, String apiPassword, ProxyInformation proxyInformation)
          throws IOException {
        this.host = host;
        httpClient = initHttpClient(proxyInformation);
        this.token = connnect(host, apiUserName, apiPassword);
    }


    public String connnect(String host, String apiUserName, String apiPassword) throws IOException {
        PostMethod method = new PostMethod(host + "/api/1.0/authentication");
        method.addParameter("userName", apiUserName);
        method.addParameter("password", apiPassword);

        return executeHttMethod(httpClient, method);
    }


    public String getAllEvents() throws IOException {
        GetMethod getMethod = new GetMethod(host + "/api/1.0/event");
        getMethod.setRequestHeader("Authorization", token);

        return executeAndTraceResponse(httpClient, getMethod);
    }


    public String postEvent(String title, String type, String content) throws IOException {
        PostMethod method = new PostMethod(host + "/api/1.0/event");
        method.setRequestHeader("Authorization", token);
        method.setParameter("title", title);
        method.setParameter("type", type);
        method.setParameter("content", content);

        return executeAndTraceResponse(httpClient, method);
    }


    public String getEvent(String feedId) throws IOException {
        GetMethod getMethod = new GetMethod(host + "/api/1.0/event/" + feedId);
        getMethod.setRequestHeader("Authorization", token);

        return executeAndTraceResponse(httpClient, getMethod);
    }


    static String getToken(InputStream inputStream) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("result", Result.class);
        Result result = (Result)xstream.fromXML(inputStream);
        return result.getToken();
    }


    public String getToken() {
        return token;
    }


    private HttpClient initHttpClient(ProxyInformation proxy) {
        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.useragent", "Test Client");

        if (proxy != null) {
            client.getHostConfiguration().setProxy(proxy.getProxyHost(), proxy.getProxyPort());
            client.getState()
                  .setProxyCredentials(null, proxy.getProxyHost(), new UsernamePasswordCredentials(proxy.getProxyUser(),
                                                                                                   proxy.getProxyPassword()));
            client.getState().setAuthenticationPreemptive(true);
        }
        return client;
    }


    private String executeAndTraceResponse(HttpClient client, HttpMethod getMethod) throws IOException {
        try {
            int returnCode = client.executeMethod(getMethod);

            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                System.err.println("The Post method is not implemented by this URI");
                // still consume the response body
                return getMethod.getResponseBodyAsString();
            }
            else {
                return readResponseStream(getMethod.getResponseBodyAsStream());
            }
        }
        finally {
            getMethod.releaseConnection();
        }
    }


    private String executeHttMethod(HttpClient client, HttpMethod method) throws IOException {
        try {
            int returnCode = client.executeMethod(method);

            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                System.err.println("The Post method is not implemented by this URI");
                // still consume the response body
                return method.getResponseBodyAsString();
            }
            else {
                return getToken(method.getResponseBodyAsStream());
            }
        }
        finally {
            method.releaseConnection();
        }
    }


    private String readResponseStream(InputStream responseBodyAsStream) throws IOException {
        BufferedReader br = null;
        StringBuilder builder = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(responseBodyAsStream));
            String readLine;
            while (((readLine = br.readLine()) != null)) {
                builder.append(readLine).append("\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                br.close();
            }
        }
        return builder.toString();
    }
}

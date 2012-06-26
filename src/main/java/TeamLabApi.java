import bean.Result;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: marcona
 * Date: 26/06/12
 * Time: 22:08
 * To change this template use File | Settings | File Templates.
 */
public class TeamLabApi {
    private String host;
    private String token;


    public TeamLabApi(String host, String apiUserName, String apiPassword) throws IOException {
        this.host = host;
        this.token = connnect(host, apiUserName, apiPassword);
    }

    public String connnect(String host, String apiUserName, String apiPassword) throws IOException {
        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.useragent", "Test Client");

        PostMethod method = new PostMethod(host + "/api/1.0/authentication");

        method.addParameter("userName", apiUserName);
        method.addParameter("password", apiPassword);

        try {
            int returnCode = client.executeMethod(method);

            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                System.err.println("The Post method is not implemented by this URI");
                // still consume the response body
                return method.getResponseBodyAsString();
            } else {
                return getToken(method.getResponseBodyAsStream());
                }
        } finally {
            method.releaseConnection();
        }
    }

    public String getAllEvents() throws IOException {
        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.useragent", "Test Client");

        GetMethod getMethod = new GetMethod(host + "/api/1.0/event");
        System.out.println("token = " + token);
        getMethod.setRequestHeader("Authorization",token);

        try {
            int returnCode = client.executeMethod(getMethod);

            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                System.err.println("The Post method is not implemented by this URI");
                // still consume the response body
                return getMethod.getResponseBodyAsString();
            } else {
                return readResponseStream(getMethod.getResponseBodyAsStream());
            }
        } finally {
            getMethod.releaseConnection();
        }
    }

    public String postEvent(String host, String content) throws IOException {
        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.useragent", "Test Client");

        PostMethod method = new PostMethod(host + "/api/1.0/event");
        method.setRequestHeader("Authorization",token);

        method.setRequestEntity(new StringRequestEntity(content,"application/xml","utf-8"));
        try {
            int returnCode = client.executeMethod(method);

            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                System.err.println("The Post method is not implemented by this URI");
                // still consume the response body
                return method.getResponseBodyAsString();
            } else {
                return readResponseStream(method.getResponseBodyAsStream());
            }
        } finally {
            method.releaseConnection();
        }
    }

    public String getEvent(String feedId) throws IOException {
        HttpClient client = new HttpClient();
        client.getParams().setParameter("http.useragent", "Test Client");

        GetMethod getMethod = new GetMethod(host + "/api/1.0/event/"+feedId);
        System.out.println("token = " + token);
        getMethod.setRequestHeader("Authorization",token);

        try {
            int returnCode = client.executeMethod(getMethod);

            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                System.err.println("The Post method is not implemented by this URI");
                // still consume the response body
                return getMethod.getResponseBodyAsString();
            } else {
                return readResponseStream(getMethod.getResponseBodyAsStream());
            }
        } finally {
            getMethod.releaseConnection();
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
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return builder.toString();
    }

    static String getToken(InputStream inputStream) {
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("result", Result.class);
        Result result = (Result) xstream.fromXML(inputStream);
        return result.getToken();
    }

    public String getToken() {
        return token;
    }
}

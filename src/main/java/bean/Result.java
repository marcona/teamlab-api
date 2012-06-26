package bean;

/**
 * Created with IntelliJ IDEA.
 * User: marcona
 * Date: 26/06/12
 * Time: 22:40
 * To change this template use File | Settings | File Templates.
 */
public class Result {
    private AuthentResponse response;
    private int statusCode;
    private int status;
    private int startIndex;
    private int count;

    public String getToken() {
        return response.getToken();
    }
}

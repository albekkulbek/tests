package utils;

import io.restassured.response.Response;
import ru.yandex.qatools.allure.annotations.Attachment;
import static io.restassured.RestAssured.given;


public class Api {


    private static String host = "http://localhost:8082";

    private String path;

    public Api (String path){
        this.path=path;
    }


    public  Response post(String body) {
        Response response = given().
                contentType("application/json").
                body(body).
                when().post(host+path);

        String log = "[POST] URL= " + host+path + "\n"
                + "Body:\n" + body + "\n"
                + "-------------------" + "\n"
                + "Response Code = " + response.getStatusCode() + "\n"
                + "Response Body:\n" + response.getBody().asString() + "\n"
                + "===================" + "\n";
        attachResponseLog("[Log] " + host+path, log);
        return response;
    }

    @Attachment(value = "{0}", type = "text/plain")
    private static String attachResponseLog(String attachName, String message) {
        return message;
    }
}

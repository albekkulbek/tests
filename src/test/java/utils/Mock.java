package utils;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import static utils.PropLoader.*;


public class Mock {


    private static WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(8082));

    public static void startWireMock() {
        wireMockServer.start();
        wireMockServer.addMockServiceRequestListener(new RequestListener() {
            public void requestReceived(Request request, com.github.tomakehurst.wiremock.http.Response response) {
                saveRequests(request);
            }
        });

        wireMockServer.stubFor(post(urlEqualTo("/add")).willReturn(aResponse().
                withStatus(400).withHeader("Content-Type", "application/json")
                .withBody("{\"code\":400,\"description\":\"Bad Request\"}")
        ));

        wireMockServer.stubFor(post(urlEqualTo("/add")).
                withRequestBody(containing(getS("productIdNotExist"))).
                withRequestBody(containing(getS("productName"))).
                withRequestBody(containing(getS("productPriceInteger"))).
                willReturn(okJson("{\"code\":200,\"description\":\"success\"}"))
        );

        wireMockServer.stubFor(post(urlEqualTo("/add")).
                withRequestBody(containing(getS("productIdNotExist"))).
                withRequestBody(containing(getS("productName"))).
                withRequestBody(containing(String.valueOf(getF("productPriceFloat")))).
                willReturn(okJson("{\"code\":200,\"description\":\"success\"}"))
        );

        wireMockServer.stubFor(post(urlEqualTo("/add")).
                withRequestBody(containing(getS("productIdExist"))).
                willReturn(aResponse().withStatus(500).withHeader("Content-Type", "application/json").
                        withBody("{\"code\":500,\"description\":\"Internal Server Error\"}"))
        );

        wireMockServer.stubFor(post(urlEqualTo("/add")).
                withRequestBody(matchingJsonPath("$[?(@.price == '0')]")).
                willReturn(aResponse().withStatus(500).withHeader("Content-Type", "application/json").
                        withBody("{\"code\":500,\"description\":\"Internal Server Error\"}"))
        );

        wireMockServer.stubFor(post(urlEqualTo("/add")).
                withRequestBody(containing(getS("productPriceNegative"))).
                willReturn(aResponse().withStatus(500).withHeader("Content-Type", "application/json").
                        withBody("{\"code\":500,\"description\":\"Internal Server Error\"}"))
        );

        wireMockServer.stubFor(post(urlEqualTo("/add")).
                withRequestBody(containing(String.valueOf(getF("productPriceNegativeFloat")))).
                willReturn(aResponse().withStatus(500).withHeader("Content-Type", "application/json").
                        withBody("{\"code\":500,\"description\":\"Internal Server Error\"}"))
        );


        wireMockServer.stubFor(post(urlEqualTo("/get")).willReturn(aResponse().
                withStatus(400).withHeader("Content-Type", "application/json")
                .withBody("{\"code\":400,\"description\":\"Bad Request\"}")
        ));

        wireMockServer.stubFor(post(urlEqualTo("/get")).
                withRequestBody(containing(getS("productIdNotExist"))).
                willReturn(aResponse().withStatus(404).withHeader("Content-Type", "application/json").
                        withBody("{\"code\":404,\"description\":\"Not Found\"}"))
        );

        wireMockServer.stubFor(post(urlEqualTo("/get")).
                withRequestBody(containing(getS("productIdExist"))).
                willReturn(okJson("{\"id\":" + getI("productIdExist") + "," +
                        "\"name\":\"" + getS("productName") + "\",\"price\":" + getF("productPriceFloat") + "}"))
        );

    }

    public static void stopWireMock() {
        wireMockServer.stop();
    }

    private static void saveRequests(Request request) {
        String date = new SimpleDateFormat("dd.MM.YY HH:mm:ss.S").format(new Date());
        System.out.println(date + " Mock request: " + request.getAbsoluteUrl() + " Body: \n " + request.getBodyAsString());
    }

}


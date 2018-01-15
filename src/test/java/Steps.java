import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import net.minidev.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import utils.Api;

import static org.hamcrest.core.IsEqual.equalTo;
import static utils.PropLoader.*;

class Steps {

    private void checkResponse(Response response, int status, Object... objects) {
        ValidatableResponse validatableResponse = response.then().assertThat().statusCode(status);
        for (int i = 0; i < objects.length; i += 2) {
            validatableResponse.body(String.valueOf(objects[i]), equalTo(objects[i + 1]));
        }
    }

    private Api apiAdd = new Api("/add");
    private Api apiGet = new Api("/get");

    //Test1. Проверка метода добавления товаров

    private String createJsonAdd(Integer id, String name, Object price) {
        JSONObject jsonObject = new JSONObject();
        if (id != null) jsonObject.put("id", id);
        if (name != null) jsonObject.put("name", name);
        if (price != null) jsonObject.put("price", price);
        return jsonObject.toJSONString();
    }

    @Step("Шаг 1. Успешное добавление с целочисленным значением цены")
    void t1s1() {
        Response response = apiAdd.post(createJsonAdd(getI("productIdNotExist"), getS("productName"), getS("productPriceInteger")));
        checkResponse(response, 200, "code", 200, "description", "success");
    }

    @Step("Шаг 2. Успешное добавление с дробной ценой")
    public void t1s2() {
        Response response = apiAdd.post(createJsonAdd(getI("productIdNotExist"), getS("productName"), getF("productPriceFloat")));
        checkResponse(response, 200, "code", 200, "description", "success");
    }

    @Step("Шаг 3. Добавление с передачей существующего id")
    void t1s3() {
        Response response = apiAdd.post(createJsonAdd(getI("productIdExist"), getS("productName"), getS("productPriceInteger")));
        checkResponse(response, 500, "code", 500, "description", "Internal Server Error");
    }

    @Step("Шаг 4. Добавление. price = 0")
    void t1s4() {
        Response response = apiAdd.post(createJsonAdd(getI("productIdNotExist"), getS("productName"), getI("productPriceNull")));
        checkResponse(response, 500, "code", 500, "description", "Internal Server Error");
    }

    @Step("Шаг 5. Добавление. price отрицательный")
    void t1s5() {
        Response response = apiAdd.post(createJsonAdd(getI("productIdNotExist"), getS("productName"), getI("productPriceNegative")));
        checkResponse(response, 500, "code", 500, "description", "Internal Server Error");
    }

    @Step("Шаг 6. Добавление. price отрицательный, с точкой")
    void t1s6() {
        Response response = apiAdd.post(createJsonAdd(getI("productIdNotExist"), getS("productName"), getF("productPriceNegativeFloat")));
        checkResponse(response, 500, "code", 500, "description", "Internal Server Error");
    }

    @Step("Шаг 7. Добавление. id отсутствует")
    void t1s7() {
        Response response = apiAdd.post(createJsonAdd(null, getS("productName"), getS("productPriceInteger")));
        checkResponse(response, 400, "code", 400, "description", "Bad Request");
    }

    @Step("Шаг 8. Добавление. name отсутствует")
    void t1s8() {
        Response response = apiAdd.post(createJsonAdd(getI("productIdNotExist"), null, getS("productPriceInteger")));
        checkResponse(response, 400, "code", 400, "description", "Bad Request");
    }

    @Step("Шаг 9. Добавление. price отсутствует")
    void t1s9() {
        Response response = apiAdd.post(createJsonAdd(getI("productIdNotExist"), getS("productName"), null));
        checkResponse(response, 400, "code", 400, "description", "Bad Request");
    }

    @Step("Шаг 10. Добавление. параметры отсутствуют")
    void t1s10() {
        Response response = apiAdd.post(createJsonAdd(null, null, null));
        checkResponse(response, 400, "code", 400, "description", "Bad Request");
    }

    @Step("Шаг 11. Добавление. body отсутствует")
    void t1s11() {
        Response response = apiAdd.post("");
        checkResponse(response, 400, "code", 400, "description", "Bad Request");
    }


    //Test 2. Проверка метода получения данных о товаре

    private String createJsonGet(Integer id) {
        JSONObject jsonObject = new JSONObject();
        if (id != null) jsonObject.put("id", id);
        return jsonObject.toJSONString();
    }

    @Step("Шаг 1. Успешное получение данных о товаре")
    void t2s1() {
        Response response = apiGet.post(createJsonGet(getI("productIdExist")));
        checkResponse(response, 200,
                "id", getI("productIdExist"),
                "name", getS("productName"),
                "price", getF("productPriceFloat")
        );
    }

    @Step("Шаг 2. Получение данных. По id товар отсутствует")
    void t2s2() {
        Response response = apiGet.post(createJsonGet(getI("productIdNotExist")));
        checkResponse(response, 404, "code", 404, "description", "Not Found");
    }

    @Step("Шаг 3. Получение данных. id не передается. В запросе отсутствуют обязятельные параметры")
    void t2s3() {
        Response response = apiGet.post(createJsonGet(null));
        checkResponse(response, 400, "code", 400, "description", "Bad Request");
    }

    @Step("Шаг 4. Получение данных. body отсутствует.")
    void t2s4() {
        Response response = apiGet.post("");
        checkResponse(response, 400, "code", 400, "description", "Bad Request");
    }

}

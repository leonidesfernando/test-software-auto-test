package br.com.home.lab.softwaretesting.automation.restassured;

import br.com.home.lab.softwaretesting.automation.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public final class RestAssurredUtil {

    record Credentials(String username, String password){}

    record LoginApiResponse(String token, String type, int id, String username, String email, List<String> roles){}

    public static String doLogin(User user, String endPoint){
        LoginApiResponse apiResponse = callLogin(user, endPoint)
                .getBody().as(LoginApiResponse.class);
        return apiResponse.token;
    }

    private static Response callLogin(User user, String endPoint){
        var jsonBody = asJson(new Credentials(user.username(), user.password()));
        return given()
                .config(RestAssuredConfig.config()
                        .encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8")))
                .contentType("application/json; charset=utf-8")
                .when().body(jsonBody)
                .request(Method.POST, endPoint);
    }

    public static Response post(String token, String endPoint, Object body) {
        return doRequestWithBody(token, HttpMethod.POST, endPoint, body);
    }

    public static Response put(String token, String endPoint, Object body) {
        return doRequestWithBody(token, HttpMethod.PUT, endPoint, body);
    }


    public static Response get(String token, String endpoint) {
        return baseRequest(token)
                .get(endpoint);
    }

    public static Response delete(String token, String endpoint) {
        return baseRequest(token)
                .delete(endpoint);
    }


    public static Response get(String token, List<Pair<String, String>> params, String endPoint) {
        RequestSpecification specification = baseRequest(token);
        params.forEach(p -> specification.pathParam(p.getLeft(), p.getRight()));
        return specification.get(endPoint);
    }

    private static Response doRequestWithBody(String token, HttpMethod method, String endpoint, Object bodyParam) {
        return baseRequest(token)
                .body(asJson(bodyParam))
                .request(method.name(), endpoint);
    }

    private static RequestSpecification baseRequest(String token){
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer "+token);
    }

    @Deprecated
    private void addFormParams(RequestSpecification requestSpecification, Map<String, String> formParams) {
        formParams.entrySet()
                .forEach(entry ->
                        requestSpecification
                                .formParam(entry.getKey(), entry.getValue()));
    }


    @SneakyThrows
    private static String asJson(Object data){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(data);
    }

    public static <T> T extractDataFromBodyResponse(Response response, TypeReference<T> typeReference){
        return RestAssurredUtil.extractDataFromBodyResponseByTypeReference(response, typeReference);
    }

    private static <T> T extractDataFromBodyResponseByTypeReference(Response response, TypeReference<T> type) {
        var jsonNode = response.body().as(JsonNode.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(jsonNode, type);
    }
}

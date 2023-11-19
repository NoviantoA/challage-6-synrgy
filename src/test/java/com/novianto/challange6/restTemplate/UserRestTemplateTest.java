package com.novianto.challange6.restTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novianto.challange6.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRestTemplateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    public UserService userService;

    @Test
    public void listSukses() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("Content-Type", "application/json");


        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/user/all-users?size=10&page=0", HttpMethod.GET, null, String.class);
        System.out.println("response  =" + exchange.getBody()); //JACKSON Parsing
        assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }

    @Test
    public void getIdSukses() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("Content-Type", "application/json");
        String id = "03784b0b-fe6f-462a-951f-a3f5e3d34891";
        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/user/get/" + id, HttpMethod.GET, null, String.class);
        System.out.println("response  =" + exchange.getBody());
        assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }

    @Test
    public void saveSukses() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");

        String requestBody = "{\n" +
                "    \"username\" : \"farhankebab\",\n" +
                "    \"emailAddress\" : \"kebab@gmail.com\",\n" +
                "    \"password\" : \"123\"\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/user/save", HttpMethod.POST, entity, String.class);
        System.out.println("response  =" + exchange.getBody());

        assertEquals(HttpStatus.OK, exchange.getStatusCode());

        String jsonResponse = exchange.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        String message = jsonNode.has("message") ? jsonNode.get("message").asText() : null;
        int status = jsonNode.has("status") ? jsonNode.get("status").asInt() : -1;
        JsonNode data = jsonNode.has("data") ? jsonNode.get("data") : null;
    }


    @Test
    public void updateSukses() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("Content-Type", "application/json");
        String bodyTesting = "{\n" +
                "    \"username\" : \"woeemalingpangsit\",\n" +
                "    \"emailAddress\" : \"pangsit@gmail.com\",\n" +
                "    \"password\" : \"123\"\n" +
                "}";
        HttpEntity<String> entity = new HttpEntity<String>(bodyTesting, headers);
        String id = "03784b0b-fe6f-462a-951f-a3f5e3d34891";

        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/user/update/" + id, HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.OK, exchange.getStatusCode());
        System.out.println("response  = " + exchange.getBody());
    }
}

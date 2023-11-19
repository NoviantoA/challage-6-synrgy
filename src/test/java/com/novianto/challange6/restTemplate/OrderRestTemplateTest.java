package com.novianto.challange6.restTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novianto.challange6.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderRestTemplateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    public OrderService orderService;

    @Test
    public void listSukses() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("Content-Type", "application/json");


        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/order/all-orders?size=10&page=0", HttpMethod.GET, null, String.class);
        System.out.println("response  = " + exchange.getBody());
        assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }

    @Test
    public void getIdSukses() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("Content-Type", "application/json");
        String id = "4e536bc0-0fe6-46e0-a55f-4574e30eaa4a";
        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/order/get/" + id, HttpMethod.GET, null, String.class);
        System.out.println("response  =" + exchange.getBody());
        assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }

    @Test
    public void saveSukses() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");

        String requestBody = "{\n" +
                "    \"orderTime\": \"2023-11-01\",\n" +
                "    \"destinationAddress\": \"Jl. Jalan Desa Mlaku\",\n" +
                "    \"user\": {\n" +
                "        \"id\": \"4e536bc0-0fe6-46e0-a55f-4574e30eaa4a\"\n" +
                "    }\n" +
                "}\n";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/order/save", HttpMethod.POST, entity, String.class);
        System.out.println("response  =" + exchange.getBody());

        assertEquals(HttpStatus.OK, exchange.getStatusCode());

        String jsonResponse = exchange.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        String message = jsonNode.has("message") ? jsonNode.get("message").asText() : null;
        int status = jsonNode.has("status") ? jsonNode.get("status").asInt() : -1;
        JsonNode data = jsonNode.has("data") ? jsonNode.get("data") : null;

        if (data != null && !data.isNull()) {
            JsonNode order = data.has("order") ? data.get("order") : null;
            if (order != null && !order.isNull()) {
                JsonNode detailUser = order.has("detailUser") ? order.get("detailUser") : null;
            }
        }
    }


    @Test
    public void updateSukses() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("Content-Type", "application/json");
        String bodyTesting = "{\n" +
                "    \"orderTime\": \"2023-11-17\",\n" +
                "    \"destinationAddress\": \"Jl. Jalan Desa Mlaku\",\n" +
                "    \"user\": {\n" +
                "        \"id\": \"03784b0b-fe6f-462a-951f-a3f5e3d34891\"\n" +
                "    }\n" +
                "}\n";
        HttpEntity<String> entity = new HttpEntity<String>(bodyTesting, headers);
        String id = "1e958684-7d93-4aea-a310-6e0eecc9cc88";

        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/order/update/" + id, HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.OK, exchange.getStatusCode());
        System.out.println("response  = " + exchange.getBody());
    }
}

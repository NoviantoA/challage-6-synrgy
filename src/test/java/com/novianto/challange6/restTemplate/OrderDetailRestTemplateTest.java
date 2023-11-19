package com.novianto.challange6.restTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novianto.challange6.service.OrderDetailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderDetailRestTemplateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    public OrderDetailService orderDetailService;

    @Test
    public void listSukses() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("Content-Type", "application/json");


        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/order-detail/all-order-details?size=10&page=0", HttpMethod.GET, null, String.class);
        System.out.println("response  = " + exchange.getBody());
        assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }

    @Test
    public void getIdSukses() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("Content-Type", "application/json");
        String id = "c01928fe-99e9-4859-91c8-d355cc17d051";
        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/order-detail/get/" + id, HttpMethod.GET, null, String.class);
        System.out.println("response  =" + exchange.getBody());
        assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }

    @Test
    public void saveSukses() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");

        String requestBody = "{\n" +
                "    \"quantity\" : 8,\n" +
                "    \"totalPrice\" : 430000.00,\n" +
                "    \"product\" : {\n" +
                "        \"id\" : \"ef4d1399-8123-4704-8ce7-68f3c2b1eafc\"\n" +
                "    },\n" +
                "    \"order\" : {\n" +
                "        \"id\" : \"1e958684-7d93-4aea-a310-6e0eecc9cc88\"\n" +
                "    }\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/order-detail/save", HttpMethod.POST, entity, String.class);
        System.out.println("response  =" + exchange.getBody());

        assertEquals(HttpStatus.OK, exchange.getStatusCode());

        String jsonResponse = exchange.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        String message = jsonNode.has("message") ? jsonNode.get("message").asText() : null;
        int status = jsonNode.has("status") ? jsonNode.get("status").asInt() : -1;
        JsonNode data = jsonNode.has("data") ? jsonNode.get("data") : null;

        if (data != null && !data.isNull()) {
            JsonNode orderDetail = data.has("orderDetail") ? data.get("orderDetail") : null;
            if (orderDetail != null && !orderDetail.isNull()) {
                JsonNode detailProduct = orderDetail.has("detailProduct") ? orderDetail.get("detailProduct") : null;
            }
            if (orderDetail != null && !orderDetail.isNull()) {
                JsonNode detailOrder = orderDetail.has("detailOrder") ? orderDetail.get("detailOrder") : null;
            }
        }
    }


    @Test
    public void updateSukses() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("Content-Type", "application/json");
        String bodyTesting = "{\n" +
                "    \"quantity\" : 2,\n" +
                "    \"totalPrice\" : 60000.00,\n" +
                "    \"product\" : {\n" +
                "        \"id\" : \"ef4d1399-8123-4704-8ce7-68f3c2b1eafc\"\n" +
                "    },\n" +
                "    \"order\" : {\n" +
                "        \"id\" : \"1e958684-7d93-4aea-a310-6e0eecc9cc88\"\n" +
                "    }\n" +
                "}";
        HttpEntity<String> entity = new HttpEntity<String>(bodyTesting, headers);
        String id = "c01928fe-99e9-4859-91c8-d355cc17d051";

        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/order-detail/update/" + id, HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.OK, exchange.getStatusCode());
        System.out.println("response  = " + exchange.getBody());
    }
}

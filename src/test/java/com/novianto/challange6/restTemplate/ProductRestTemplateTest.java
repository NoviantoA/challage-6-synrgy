package com.novianto.challange6.restTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novianto.challange6.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductRestTemplateTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    public ProductService productService;

    @Test
    public void listSukses() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("Content-Type", "application/json");


        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/product/all-products?size=10&page=0", HttpMethod.GET, null, String.class);
        System.out.println("response  = " + exchange.getBody());
        assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }

    @Test
    public void getIdSukses() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("Content-Type", "application/json");
        String id = "ef4d1399-8123-4704-8ce7-68f3c2b1eafc";
        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/product/get/" + id, HttpMethod.GET, null, String.class);
        System.out.println("response  =" + exchange.getBody());
        assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }

    @Test
    public void saveSukses() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");

        String requestBody = "{\n" +
                "    \"productName\" : \"Kopi Ireng\",\n" +
                "    \"price\" : 7000.00,\n" +
                "    \"merchant\" : {\n" +
                "        \"id\" : \"05dbe10f-4818-44d7-bc4e-78a527d68259\"\n" +
                "    }\n" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/product/save", HttpMethod.POST, entity, String.class);
        System.out.println("response  =" + exchange.getBody());

        assertEquals(HttpStatus.OK, exchange.getStatusCode());

        String jsonResponse = exchange.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        String message = jsonNode.has("message") ? jsonNode.get("message").asText() : null;
        int status = jsonNode.has("status") ? jsonNode.get("status").asInt() : -1;
        JsonNode data = jsonNode.has("data") ? jsonNode.get("data") : null;

        if (data != null && !data.isNull()) {
            JsonNode merchant = data.has("merchant") ? data.get("merchant") : null;
            if (merchant != null && !merchant.isNull()) {
                JsonNode detailMerchant = merchant.has("detailMerchant") ? merchant.get("detailMerchant") : null;
            }
        }
    }

    @Test
    public void updateSukses() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("Content-Type", "application/json");
        String bodyTesting = "{\n" +
                "    \"productName\" : \"Kopi Ireng Asli Jawa\",\n" +
                "    \"price\" : 8000.00\n" +
                "}";
        HttpEntity<String> entity = new HttpEntity<String>(bodyTesting, headers);
        String id = "ef4d1399-8123-4704-8ce7-68f3c2b1eafc";

        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/product/update/" + id, HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.OK, exchange.getStatusCode());
        System.out.println("response  = " + exchange.getBody());
    }
}

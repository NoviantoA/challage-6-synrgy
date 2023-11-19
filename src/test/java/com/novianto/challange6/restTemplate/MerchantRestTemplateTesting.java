package com.novianto.challange6.restTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novianto.challange6.service.MerchantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MerchantRestTemplateTesting {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
//    @Qualifier("merchantRestTemplate")
    public MerchantService merchantService;

    @Test
    public void test1() {
        Map map = merchantService.saveMerchant(null);
        System.out.println("ekpektasi response = " + map);
    }

    @Test
    public void listSukses() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("Content-Type", "application/json");


        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/merchant/all-merchants?size=10&page=0", HttpMethod.GET, null, String.class);
        System.out.println("response  =" + exchange.getBody()); //JACKSON Parsing
        assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }

    @Test
    public void getIdSukses() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "*/*");
        headers.set("Content-Type", "application/json");
        String id = "cf6ea358-d18f-416f-be6c-c7e7760b6b58";
        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/merchant/get/" + id, HttpMethod.GET, null, String.class);
        System.out.println("response  =" + exchange.getBody());
        assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }

    @Test
    public void saveSukses() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");

        String requestBody = "{\"merchantName\": \"Indoramen\", \"merchantLocation\": \"Malang\"}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/merchant/save", HttpMethod.POST, entity, String.class);
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
                "    \"merchantName\" : \"HomeEssentials Update\",\n" +
                "    \"merchantLocation\" : \"Jember Utara\"\n" +
                "}";
        HttpEntity<String> entity = new HttpEntity<String>(bodyTesting, headers);
        String id = "cf6ea358-d18f-416f-be6c-c7e7760b6b58";

        ResponseEntity<String> exchange = restTemplate.exchange("http://localhost:8080/api/v1/merchant/update/" + id, HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.OK, exchange.getStatusCode());
        System.out.println("response  = " + exchange.getBody());
    }

}

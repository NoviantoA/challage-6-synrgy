package com.novianto.challange6.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestingLoginController {
    @Autowired
    private TestRestTemplate restTemplate;
    @Test
    public void restTemplateLogin() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", "user@mail.com");
        map.add("password", "password");
        map.add("grant_type", "password");
        map.add("client_id", "my-client-web");
        map.add("client_secret", "password");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/api/oauth/token", request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println("response =" + response.getBody());
    }
}

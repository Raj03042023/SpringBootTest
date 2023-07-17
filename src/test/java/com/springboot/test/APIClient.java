package com.springboot.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

    @Component
    @Scope(SCOPE_CUCUMBER_GLUE)
    public class APIClient {

        private final Logger log = LoggerFactory.getLogger(APIClient.class);
        private final String SERVER_URL = "https://api.github.com/";
        private final String USERS_ENDPOINT = "/users";
        private final String REPOS_ENDPOINT = "/user/repos";
        private final String REPOS_FOR_DELETE_ENDPOINT = "repos";

        private final RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        ObjectMapper mapper = new ObjectMapper();
        private User userJsonObject=null;
        public String exception=null;
        public String responseBodyJson;
        public String responsecode;
        @Autowired
        private Environment environment;

        private String usersEndpoint(String user) {
            return SERVER_URL + USERS_ENDPOINT + user;
        }
        private String reposEndpoint() {
            return SERVER_URL + REPOS_ENDPOINT;
        }
        private String deleteEndpoint(String user, String repoName) {
            String url="";
            url= SERVER_URL + REPOS_FOR_DELETE_ENDPOINT + user + repoName;
            log.info("Delete URL: '{}'", url);
            return url;
        }

        public void users(String user) {
            try {
                String body = null;
                log.info("Token ='{}'", environment.getProperty("bearer.token"));
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.set("Authorization", "Bearer " + environment.getProperty("bearer.token"));

                HttpEntity <String> request = new HttpEntity<String>(headers);
                response = restTemplate.exchange(usersEndpoint(user), HttpMethod.GET, request, String.class);

                responseBodyJson = response.getBody();
                log.info("Received Response JSON is {}", responseBodyJson);
                try {
                    userJsonObject = mapper.readValue(responseBodyJson, User.class);
                    log.info("Retrieved Value {}", userJsonObject.id);
                } catch (JsonProcessingException exp) {
                    log.error("JsonProcessingException generated details =  {}", exp.toString());
                }
            } catch (HttpClientErrorException exp) {
                exception = exp.toString();
                log.error("HttpClientErrorException generated details =  {}", exp.toString());
            }
        }

        public String responsecode() {
            String code = response.getStatusCode().toString();
            log.info("Received Response is {}", code);
            return code;
        }
        public String responseBody() {
            return responseBodyJson;
        }
        public String getPropertyValue(String property) {
            JsonNode root, value = null;
            try {
                root = mapper.readTree(response.getBody());
                value = root.path(property);
                log.info("Retrieved Value {}", value);
            }catch(JsonProcessingException exp){
                log.error("JsonProcessingException generated details =  {}", exp.toString());
            }
            return value.asText();
        }

        public int getID() {
            return userJsonObject.id;
        }
        public String getLogin() {
            return userJsonObject.login;
        }
        public void repos(String repoName) {
            try {
                String body = null;
                log.info("Token ='{}'", environment.getProperty("bearer.token"));
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + environment.getProperty("bearer.token"));
                MultiValueMap<String, String> requestbody = new LinkedMultiValueMap<String, String>();
                requestbody.add("name", repoName);
                HttpEntity<Object> request = new HttpEntity<Object>(requestbody, headers);
                response = restTemplate.postForEntity(reposEndpoint(), request, String.class);
                body = response.getBody();
                log.info("Received Response is {}", body);
                try {
                    userJsonObject = mapper.readValue(body, User.class);
                    log.info("Retrieved Value {}", userJsonObject.id);
                } catch (JsonProcessingException exp) {
                    log.error("JsonProcessingException generated details =  {}", exp.toString());
                }
            } catch (HttpClientErrorException exp) {
                exception = exp.toString();
                log.error("HttpClientErrorException generated details =  {}", exp.toString());
            }
        }

        public void deleteRepo(String user, String repoName) {
            try {
                String body = null;
                log.info("Token ='{}'", environment.getProperty("bearer.token"));
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + environment.getProperty("bearer.token"));
                HttpEntity <String> request = new HttpEntity<String>(headers);
                response = restTemplate.exchange(deleteEndpoint(user, repoName), HttpMethod.DELETE, request, String.class);
                responsecode = response.getStatusCode().toString();
                log.info("responsecode='{}'", responsecode);
            } catch (HttpClientErrorException exp) {
                exception = exp.toString();
                log.error("HttpClientErrorException generated details =  {}", exp.toString());
            }
        }
    }

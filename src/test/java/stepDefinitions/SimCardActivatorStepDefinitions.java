package stepDefinitions;

import au.com.telstra.simcardactivator.SimCardActivator;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;

import io.cucumber.java.en.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = SimCardActivator.class, loader = SpringBootContextLoader.class)
public class SimCardActivatorStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;

    private String iccid;
    private String customerEmail;
    private ResponseEntity<String> activationResponse;
    private ResponseEntity<Map> queryResponse;

    @Given("the SIM ICCID {string} and customer email {string}")
    public void givenSimDetails(String iccid, String email) {
        this.iccid = iccid;
        this.customerEmail = email;
    }

    @When("I send an activation request to the microservice")
    public void sendActivationRequest() {
        String url = "http://localhost:8080/activate";
        Map<String, String> requestBody = Map.of(
            "iccid", iccid,
            "customerEmail", customerEmail
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        activationResponse = restTemplate.postForEntity(url, request, String.class);
    }

    @Then("the activation response should be successful")
    public void checkActivationSuccess() {
        assertEquals(HttpStatus.OK, activationResponse.getStatusCode(), "Activation response status should be 200 OK");
        assertTrue(activationResponse.getBody().contains("successful"), "Activation response should indicate success");
    }

    @Then("the activation response should indicate failure")
    public void checkActivationFailure() {
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, activationResponse.getStatusCode(), "Activation response status should be 500");
        assertTrue(activationResponse.getBody().contains("failed"), "Activation response should indicate failure");
    }

    @Then("the activation record with ID {int} should have status {string}")
    public void checkActivationRecordStatus(int id, String expectedStatus) {
        String url = "http://localhost:8080/activations/" + id;

        queryResponse = restTemplate.getForEntity(url, Map.class);

        assertEquals(HttpStatus.OK, queryResponse.getStatusCode(), "Query response should be 200 OK");

        Map<String, Object> responseBody = queryResponse.getBody();
        assertNotNull(responseBody, "Response body should not be null");

        String actualStatus = (String) responseBody.get("status");

        assertEquals(expectedStatus, actualStatus, "Activation record status should match");
    }
}

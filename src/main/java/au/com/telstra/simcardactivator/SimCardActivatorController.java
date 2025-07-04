package au.com.telstra.simcardactivator;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class SimCardActivatorController {

    private final String actuatorUrl = "http://localhost:8444/actuate";

    @PostMapping("/activate")
    public ActivationResult activateSim(@RequestBody ActivationRequest request) {

        RestTemplate restTemplate = new RestTemplate();

        ActivationRequestToActuator body = new ActivationRequestToActuator(request.getIccid());
        HttpEntity<ActivationRequestToActuator> entity = new HttpEntity<>(body);

        ResponseEntity<ActivationResult> response = restTemplate.postForEntity(
            actuatorUrl,
            entity,
            ActivationResult.class
        );

        return response.getBody();
    }
}

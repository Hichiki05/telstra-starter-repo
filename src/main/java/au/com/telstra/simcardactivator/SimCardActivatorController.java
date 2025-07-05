package au.com.telstra.simcardactivator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

@RestController
public class SimCardActivatorController {

    private final String actuatorUrl = "http://localhost:8444/actuate";

    @Autowired
    private SimCardRecordRepository simCardRecordRepository;

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

        boolean isActive = response.getBody().isSuccess();

        // Save to database
        SimCardRecord record = new SimCardRecord(
            request.getIccid(),
            request.getCustomerEmail(),
            isActive
        );

        simCardRecordRepository.save(record);

        return response.getBody();
    }

    @GetMapping("/activation")
public ResponseEntity<?> getActivation(@RequestParam Long simCardId) {
    return simCardRecordRepository.findById(simCardId)
        .<ResponseEntity<?>>map(record -> ResponseEntity.ok().body(record))
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("SIM card not found"));
}

    }


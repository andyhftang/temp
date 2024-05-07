import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class HealthCheckController {

    private final RestTemplate restTemplate;

    public HealthCheckController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/health-check")
    public ResponseEntity<String> healthCheck(@RequestBody HealthCheckRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(new URI(request.getUrl()), HttpMethod.valueOf(request.getMethod()), entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok("Health check passed");
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Health check failed");
            }
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Invalid URL");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid HTTP method");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    public static class HealthCheckRequest {
        private String url;
        private String method;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }
    }
}

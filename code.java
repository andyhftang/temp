import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class DNSResolutionController {

    @GetMapping("/resolve-dns")
    public String resolveDNS(@RequestParam String url, @RequestParam String dnsServer) {
        try {
            InetAddress inetAddress;
            if (isValidIP(dnsServer)) {
                inetAddress = InetAddress.getByAddress(url, InetAddress.getByName(dnsServer).getAddress());
            } else {
                inetAddress = InetAddress.getByAddress(url, InetAddress.getByName(dnsServer).getHostAddress());
            }
            return inetAddress.getHostAddress(); // Return the IP address
        } catch (UnknownHostException e) {
            return "Could not resolve DNS for " + url;
        }
    }

    private boolean isValidIP(String ip) {
        try {
            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }
            for (String part : parts) {
                int value = Integer.parseInt(part);
                if (value < 0 || value > 255) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

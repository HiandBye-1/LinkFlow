package Jar.analytics.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service 
public class VisitorFingerPrintService {
    
    private final String secret;


    public VisitorFingerPrintService(@Value("${app.security.hmac-secret}")String secret) {
        this.secret = secret;
    }

    public String generateFingerPrint(String ip, String userAgent)throws NoSuchAlgorithmException, InvalidKeyException {

        String combine = ip+"|"+userAgent;
        Mac mac = Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);

        byte[] result = mac.doFinal(combine.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(result);
    }

}

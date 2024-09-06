package dev.doel.TDoh.encryptations;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import dev.doel.TDoh.encryptations.base64.Base64Encoder;

@Component
public class EncoderFacade implements IEncryptFacade {

    PasswordEncoder bCryptPasswordEncoder;
    Base64Encoder base64Encoder;

    public EncoderFacade(PasswordEncoder bCryptPasswordEncoder, Base64Encoder base64Encoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.base64Encoder = base64Encoder;
    }

    public String encode(String data, String type) {

        String dataEncrypted = "";

        if (type == "bcrypt")
            dataEncrypted = bCryptPasswordEncoder.encode(data);
        if (type == "base64")
            dataEncrypted = base64Encoder.encode(data);

        return dataEncrypted;
    }

    @Override
    public String decode(String type, String data) {
        String dataDecoded = "";

        if (type == "base64")
            dataDecoded = base64Encoder.decode(data);

        return dataDecoded;

    }
}

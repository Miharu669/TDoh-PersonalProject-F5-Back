package dev.doel.TDoh.encryptations.bcrypt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dev.doel.TDoh.encryptations.IEncoder;

public class BcryptEncoder implements IEncoder {

    BCryptPasswordEncoder encoder;

    public BcryptEncoder(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public String encode(String data) {
        String dataEncoded = encoder.encode(data);
        return dataEncoded;
    }

}
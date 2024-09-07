package dev.doel.TDoh.encryptations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.doel.TDoh.encryptations.base64.Base64Encoder;

@SpringBootTest
public class EncoderFacadeTest {
    @Autowired
    EncoderFacade facade;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Mock
    private Base64Encoder base64Encoder;

    @InjectMocks
    private EncoderFacade myDecoder;

    @Test
    void testEncodeBcrypt() {
        String type = "bcrypt";
        String data = "password";

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoded = encoder.encode(data);

        when(passwordEncoder.encode(data)).thenReturn(encoded);
        String passwordEncoded = facade.encode(type, data);

        assertThat(encoder.matches(data, passwordEncoded), is(true));
    }

    @Test
    void tesBase64tEncode() {
        String type = "base64";
        String data = "user";

        Base64Encoder encoder = new Base64Encoder();
        String expectedEncoded = encoder.encode(data);
        String user = facade.encode(type, data);

        assertThat(user, is(expectedEncoded));
    }

    @Test
    void testDecodeBase64() {
        MockitoAnnotations.openMocks(this);

        String type = "base64";
        String encodedData = "dXNlcg==";
        String decodedData = "user";

        when(base64Encoder.decode(encodedData)).thenReturn(decodedData);

        String result = myDecoder.decode(type, encodedData);

        assertThat(result, is(decodedData));
    }

    @Test
    void testDecodeInvalidType() {
        MockitoAnnotations.openMocks(this);

        String type = "invalidType";
        String encodedData = "dXNlcg==";
        String result = myDecoder.decode(type, encodedData);

        assertThat(result, is(""));
    }
}

package dev.doel.TDoh.register;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.doel.TDoh.encryptations.EncoderFacade;
import dev.doel.TDoh.encryptations.IEncryptFacade;
import dev.doel.TDoh.users.User;
import dev.doel.TDoh.users.UserDto;
import dev.doel.TDoh.users.UserRepository;

@Service
public class RegisterService {
    
    private final UserRepository userRepository;
    private final IEncryptFacade encoderFacade;

    public RegisterService(UserRepository userRepository, EncoderFacade encoderFacade) {
        this.userRepository = userRepository;
        this.encoderFacade = encoderFacade;
    }

    @Transactional
    public User save(UserDto registerDto) {

        String passwordDecoded = encoderFacade.decode("base64", registerDto.getPassword());
        String passwordEncoded = encoderFacade.encode("bcrypt", passwordDecoded);

        User user = new User(0, registerDto.getUsername(), registerDto.getEmail(), passwordEncoded );

        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new IllegalArgumentException("Email already used");
        }

        return userRepository.save(user);
    }
}

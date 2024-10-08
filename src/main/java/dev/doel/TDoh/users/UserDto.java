package dev.doel.TDoh.users;

import dev.doel.TDoh.profiles.ProfileDTO;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDTO {

    private long id; 
    private String password;
    private String email;
    private ProfileDTO profile;
}

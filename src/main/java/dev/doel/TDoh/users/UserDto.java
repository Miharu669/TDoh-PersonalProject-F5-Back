package dev.doel.TDoh.users;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDTO {

    private long id; 
    private String username;
    private String password;
    private String email;
}

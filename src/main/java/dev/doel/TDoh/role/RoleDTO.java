package dev.doel.TDoh.role;

import lombok.*;
import java.util.Set;

import dev.doel.TDoh.users.UserDTO;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    
    private Long id;
    private String name;
    private Set<UserDTO> users;

}

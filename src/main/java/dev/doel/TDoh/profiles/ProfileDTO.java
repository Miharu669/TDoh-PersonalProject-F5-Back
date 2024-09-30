package dev.doel.TDoh.profiles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String country;
    private Long user;
}

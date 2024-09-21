package dev.doel.TDoh.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotBlank(message = "Username is mandatory")
    @Size(max = 20, message = "Username must be less than 20 characters")
    private String username;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is mandatory")
    // @Size(min = 6, max = 15, message = "Password must be between 6 and 15 characters")
    private String password;

    @Column(unique = true, nullable = true)
    private String googleId; 
}

package dev.doel.TDoh.profiles;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.doel.TDoh.profiles.profile_exceptions.ProfileNotFoundException;
import dev.doel.TDoh.users.User;
import jakarta.validation.Valid;

@RestController
@RequestMapping("${api-endpoint}/profiles")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<ProfileDTO>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @GetMapping("/user")
    public ResponseEntity<ProfileDTO> getProfileByUser(Principal connectedUser) {
        try {
            var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            ProfileDTO profile = profileService.getProfileByUser(user);
            return ResponseEntity.ok(profile);
        } catch (ProfileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/user")
    public ResponseEntity<ProfileDTO> updateProfile(Principal connectedUser,
            @Valid @RequestBody ProfileDTO profileDTO) {
        try {
            var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
            ProfileDTO updatedProfile = profileService.updateProfile(user, profileDTO);
            return ResponseEntity.ok(updatedProfile);
        } catch (ProfileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        try {
            profileService.deleteProfile(id);
            return ResponseEntity.noContent().build();
        } catch (ProfileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}


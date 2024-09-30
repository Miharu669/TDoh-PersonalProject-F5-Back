package dev.doel.TDoh.profiles;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.doel.TDoh.profiles.profile_exceptions.ProfileNotFoundException;
import dev.doel.TDoh.users.User;
import dev.doel.TDoh.users.UserRepository;
import dev.doel.TDoh.users.user_exceptions.UserNotFoundException;

@Service
public class ProfileService {
    
@Autowired
private ProfileRepository profileRepository;
@Autowired
private UserRepository userRepository;

public void createProfile(Long userId) {
     User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Profile profile = new Profile();
        profile.setUser(user);

        profileRepository.save(profile);
    }
    public List<ProfileDTO> getAllProfiles() {
        return profileRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ProfileDTO getProfileByUser(User user) {
        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
        return mapToDTO(profile);
    }

    public ProfileDTO updateProfile(User user, ProfileDTO profileDTO) {
        Profile existingProfile = profileRepository.findByUser(user)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
        existingProfile.setFirstName(profileDTO.getFirstName());
        existingProfile.setLastName(profileDTO.getLastName());
        existingProfile.setCountry(profileDTO.getCountry());

        Profile updatedProfile = profileRepository.save(existingProfile);
        return mapToDTO(updatedProfile);
    }

    public void deleteProfile(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new ProfileNotFoundException("Profile not found");
        }
        profileRepository.deleteById(id);

    }

    ProfileDTO mapToDTO(Profile profile) {
    
        return new ProfileDTO(
                profile.getId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getCountry(),
                profile.getUser().getId());
    
    }
}
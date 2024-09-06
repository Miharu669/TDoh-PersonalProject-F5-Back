package dev.doel.TDoh.users;

public class UserDto { 

    private String username;
    private String password;
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

}

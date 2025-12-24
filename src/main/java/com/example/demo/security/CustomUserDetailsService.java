package com.example.demo.security;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final Map<String, DemoUser> users = new HashMap<>();
    
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
        // Initialize with default admin user
        users.put("admin@city.com", new DemoUser(1L, "Admin", "admin@city.com", "admin123", "ADMIN"));
    }
    
    public CustomUserDetailsService() {
        this.userRepository = null;
        users.put("admin@city.com", new DemoUser(1L, "Admin", "admin@city.com", "admin123", "ADMIN"));
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        DemoUser user = users.get(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }
        
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())))
            .build();
    }
    
    public DemoUser getByEmail(String email) {
        return users.get(email);
    }
    
    public DemoUser registerUser(String fullName, String email, String password) {
        if (users.containsKey(email)) {
            throw new RuntimeException("User already exists");
        }
        DemoUser user = new DemoUser((long) (users.size() + 1), fullName, email, password, "USER");
        users.put(email, user);
        return user;
    }
    
    public static class DemoUser {
        private Long id;
        private String fullName;
        private String email;
        private String password;
        private String role;
        
        public DemoUser(Long id, String fullName, String email, String password, String role) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.role = role;
        }
        
        public Long getId() { return id; }
        public String getFullName() { return fullName; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getRole() { return role; }
    }
}
package com.moviez.DSII_P2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.moviez.DSII_P2.model.user.User;
import com.moviez.DSII_P2.model.user.UserRole;
import com.moviez.DSII_P2.repository.UserRepository;

@Configuration
public class DataInitializationConfig {

    @Value("${admin.email}")
    private String adminEmail;
    
    @Value("${admin.password}")
    private String adminPassword;
    
    @Value("${admin.username}")
    private String adminUsername;

    @Bean
    CommandLineRunner initializeData(UserRepository userRepository) {
        return args -> {
            // Verifica se o admin já existe
            User existingAdmin = userRepository.findByLogin(adminEmail);
            
            if (existingAdmin == null) {
                // Cria o usuário admin
                String encryptedPassword = new BCryptPasswordEncoder().encode(adminPassword);
                User admin = new User(adminEmail, encryptedPassword, UserRole.ADMIN);
                admin.setUsername(adminUsername);
                
                userRepository.save(admin);
                System.out.println("✅ Admin criado com sucesso!");
                System.out.println("   Email: " + adminEmail);
                System.out.println("   Username: " + adminUsername);
            } else {
                System.out.println("✅ Admin já existe no banco de dados");
            }
        };
    }
}

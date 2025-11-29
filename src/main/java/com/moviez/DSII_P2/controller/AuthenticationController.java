package com.moviez.DSII_P2.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.moviez.DSII_P2.model.user.RegisterDTO;
import com.moviez.DSII_P2.model.user.User;
import com.moviez.DSII_P2.repository.UserRepository;



@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login"; // nome do HTML
    }
    
    
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new RegisterDTO(null, null, null)); // <---- REQUIRED
        return "auth/register"; // nome do HTML
    }
    

    @PostMapping("/register")
    public String register(RegisterDTO data, Model model) {

        if (userRepository.findByLogin(data.login()) != null) {
            model.addAttribute("error", "Usuário já existe");
            return "auth/register";
        }

        String encrypted = new BCryptPasswordEncoder().encode(data.password());
        User user = new User(data.login(), encrypted, data.role());
        userRepository.save(user);

        // Passar credenciais para auto-login via formulário
        model.addAttribute("username", data.login());
        model.addAttribute("password", data.password());
        return "auth/auto-login";
    }    // Endpoint para verificar disponibilidade de username (AJAX)
    @GetMapping("/check-username")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {
        boolean available = !userRepository.existsByUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available);
        return ResponseEntity.ok(response);
    }
    
    // Endpoint para salvar username do usuário logado
    @PostMapping("/set-username")
    public String setUsername(@RequestParam String username, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String login = auth.getName();
            User user = userRepository.findByLogin(login);
            
            if (user != null) {
                // Verificar se username já existe
                if (userRepository.existsByUsername(username)) {
                    model.addAttribute("error", "Username já está em uso");
                    return "redirect:/movies?username_error=true";
                }
                
                user.setUsername(username);
                userRepository.save(user);
            }
        }
        return "redirect:/movies";
    }
}

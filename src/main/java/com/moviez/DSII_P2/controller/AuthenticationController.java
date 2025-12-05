package com.moviez.DSII_P2.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

     @Autowired
    private AuthenticationManager authenticationManager;

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
    public String register(RegisterDTO data, Model model, HttpServletRequest request, HttpServletResponse response) {

        if (userRepository.findByLogin(data.login()) != null) {
            return "redirect:/auth/register?usuariojaexiste=true";
        }

        String encrypted = new BCryptPasswordEncoder().encode(data.password());
        User user = new User(data.login(), encrypted, com.moviez.DSII_P2.model.user.UserRole.USER);
        
        // Salva o usuário no banco de dados
        User savedUser = userRepository.save(user);

        // Autentica o usuário automaticamente no backend (Spring Security)
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(data.login(), data.password());
            Authentication auth = authenticationManager.authenticate(authentication);

         // Coloca o usuário autenticado no SecurityContext
         SecurityContextHolder.getContext().setAuthentication(auth);
         // Garante que o contexto de segurança seja persistido na sessão
         request.getSession(true)
             .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                     SecurityContextHolder.getContext());

            // Redireciona para a página principal de filmes
            if (savedUser != null && savedUser.getId() != null) {
                return "auth/auto-login";
            } else {
                model.addAttribute("error", "Erro ao salvar usuário");
                return "auth/register";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao autenticar usuário");
            return "auth/register";
        }
    }  
    
    // Endpoint para verificar disponibilidade de username (AJAX)
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
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "redirect:/auth/login";
        }
        
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
        
        return "redirect:/movies";
    }
}

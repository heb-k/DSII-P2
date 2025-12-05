package com.moviez.DSII_P2.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        String errorMessage = "Ops! Algo deu errado.";
        String errorDescription = "Pedimos desculpas pelo inconveniente.";
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            switch (statusCode) {
                case 404:
                    errorMessage = "Página não encontrada";
                    errorDescription = "A página que você está procurando não existe.";
                    break;
                case 403:
                    errorMessage = "Acesso negado";
                    errorDescription = "Você não tem permissão para acessar esta página.";
                    break;
                case 500:
                    errorMessage = "Erro interno do servidor";
                    errorDescription = "Estamos trabalhando para resolver o problema.";
                    break;
                default:
                    errorMessage = "Erro " + statusCode;
                    errorDescription = "Algo inesperado aconteceu.";
            }
            
            model.addAttribute("statusCode", statusCode);
        }
        
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("errorDescription", errorDescription);
        
        return "error";
    }
}

package com.moviez.DSII_P2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoviesController {
  @GetMapping({ "/movies", "/movies/list" })
  public String list(Model model) {
    model.addAttribute("pageTitle", "Filmes");
    model.addAttribute("movies", java.util.List.of());
    return "movies/list";
  }

  @GetMapping("/movies/form")
  public String form(Model model) {
    model.addAttribute("pageTitle", "Novo Filme");
    return "movies/form";
  }

  @GetMapping("/movies/detail")
  public String detail(Model model) {
    model.addAttribute("pageTitle", "Detalhes do Filme");
    return "movies/detail";
  }
}

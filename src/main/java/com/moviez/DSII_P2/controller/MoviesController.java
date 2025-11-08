package com.moviez.DSII_P2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoviesController {
  @GetMapping("/movies")
  public String list(Model model) {
    model.addAttribute("pageTitle", "Filmes");
    model.addAttribute("movies", java.util.List.of()); // placeholder
    return "movies/list";
  }
}
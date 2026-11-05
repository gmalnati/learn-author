package dev.learnlab.exercise;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** A page and the fragment that htmx puts in it. */
@Controller
class HomeController {
    @GetMapping("/")
    String home(Model model) {
        model.addAttribute("name", "mondo");
        return "index";
    }

    @GetMapping("/greeting")
    String greeting(@RequestParam String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
}

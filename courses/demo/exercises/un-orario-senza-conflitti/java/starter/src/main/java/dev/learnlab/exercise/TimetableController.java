package dev.learnlab.exercise;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/** La pagina dell'orario e la griglia che htmx sostituisce dopo un nuovo calcolo. */
@Controller
class TimetableController {
    private final TimetableService service;

    TimetableController(TimetableService service) {
        this.service = service;
    }

    @GetMapping("/")
    String page(Model model) {
        model.addAttribute("timetable", service.view(service.current()));
        return "index";
    }

    @PostMapping("/timetable")
    String solve(Model model) {
        model.addAttribute("timetable", service.view(service.solve()));
        return "grid";
    }
}

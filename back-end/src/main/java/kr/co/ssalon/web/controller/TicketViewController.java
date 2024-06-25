package kr.co.ssalon.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TicketViewController {

    @GetMapping("/web/**")
    public String fetchTicketPage() {
        return "forward:/";
    }
}

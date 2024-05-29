package kr.co.ssalon.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/admin")
    public String admin() {
        return "forward:/admin/index.html";
    }
}

package ece651.RISC.Server.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/chat")
public class HomeController {

    @RequestMapping("/index.html")
    public String home() {
        return "index";
    }
}

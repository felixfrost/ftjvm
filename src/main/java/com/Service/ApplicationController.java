package Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ApplicationController {

    @Autowired
    ApplicationService service;

    @GetMapping("/")
    public String home (Model model){
        return "home";
    }

    @GetMapping("/getQuiz")
    public String apiTest(@RequestParam("amount") int amount, @RequestParam("category") int category, Model model){
        model.addAttribute("response", service.apiConnect(amount, category));
        return "apiTest";
    }

    // https://opentdb.com/api.php?amount=20&category=13
    // https://opentdb.com/api_category.php (get all categories and their id in an array)


}

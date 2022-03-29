package com.Service;

import com.Model.Question;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ApplicationController {

    @Autowired
    ApplicationService service;

    @Autowired
    UserRepository userRepo;



    @GetMapping("/")
    public String home (Model model, HttpSession session){
        User user = (User) session.getAttribute("currentUser");
        if(user == null){
            return ("redirect:/login");
        }
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/settings")
    public String settings (Model model, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if(user == null){
            return ("redirect:/login");
        }
        model.addAttribute("user", user);
        return "settings";
    }

    @GetMapping("settings/{num}")
    public String avatar(HttpSession session, @PathVariable Integer num) {
        User user = (User) session.getAttribute("currentUser");
        user.setAvatarId(num);
        return "redirect:/settings";
    }

    @GetMapping("/about-us")
    public String aboutUs (HttpSession session) {
        return "aboutUs";
    }

    @GetMapping("/createmultiplayergame")
    public String multiPlayerInit(HttpSession session){
        String gameId = RandomString.make(7);
        session.setAttribute("multiplayerHost", true);
        session.setAttribute("gameId", gameId);
        return ("redirect:/select");
    }

    @GetMapping("/multiplayer")
    public String multiPlayer (HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if(user == null){ return ("redirect:/login"); }
        return "multiplayer";
    }

    @PostMapping("/multiplayer")
    public String joinGame(String gameId){
        return "";
    }


    @GetMapping("/trivimania")
    public String game () {
        return "game";
    }

    @GetMapping("/score")
    public String score (HttpSession session, Model model){
        model.addAttribute("user", session.getAttribute("currentUser"));
        model.addAttribute("score", session.getAttribute("scoreCounter"));
        return "score";
    }

    @GetMapping("/highscore")
    public String highScore (HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if(user == null){
            return ("redirect:/login");
        }
        model.addAttribute("userTop", (HighScore)service.findUserTopScore(user.getUsername()));
        model.addAttribute("user", user);
        model.addAttribute("topScores", service.getTopScores());
        model.addAttribute("todayTop", service.getTodayTop());
        model.addAttribute("weekTop", service.getWeekTop());

        System.out.println(service.getWeekTop());
        System.out.println(service.getTodayTop());
        System.out.println(service.getTopScores());
        return "highscore";
    }

    @GetMapping("/createuser")
    public String createUser (Model model){
        model.addAttribute("user", new User());
        return "createUser";
    }

    @PostMapping("/createuser")
    public String createUser (@Valid @ModelAttribute User user, BindingResult result) {
        if(result.hasErrors()){
            return "createUser";
        }
        if(userRepo.findByUsernameEquals(user.getUsername()) == null) {
            userRepo.save(user);
            System.out.println(user.getUsername() + user.getFirstname() + user.getLastname());
            return "redirect:/";
        }
        else return "login";
    }

    @GetMapping("/login")
    public String login (){
        return "login";
    }

    @PostMapping("/login")
    public String login (HttpSession session, @RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        if (session.getAttribute("currentUser") == null && userRepo.findByUsernameEquals(username) != null) {
            if (userRepo.findByUsernameEquals(username).getPassword().equals(password)) {
                session.setAttribute("currentUser", userRepo.findByUsernameEquals(username));
                System.out.println("Success logging in...");
                return "redirect:/";
            }
        } else { System.out.println("Error logging in..."); }
        return "login";
    }

    @GetMapping("/logout")
    public String logout (HttpSession session) {
        session.setAttribute("currentUser", null);
        System.out.println("Logging out...");
        return "redirect:/";
    }

    @GetMapping("/game")
    public String getQuiz(HttpSession session, Model model) {
        Boolean multiplayerGuest = (Boolean)session.getAttribute("multiplayerGuest");
        Boolean multiplayerHost = (Boolean)session.getAttribute("multiplayerHost");
        List<Question> questions = service.getQuestions((int)session.getAttribute("limit"),(String)session.getAttribute("category"));
        //är du host?
        if(multiplayerHost != null && multiplayerHost) {
            String gameId = (String)session.getAttribute("gameId");
            service.createMultiplayerGame(gameId, (User)session.getAttribute("currentUser"), questions);
        }
        //har du joinat ett spel?
        if(multiplayerGuest != null && multiplayerGuest) {
            String gameId = (String)session.getAttribute("gameId");
            questions = service.getMultiplayerQuestions(gameId);
            service.joinMultiplayerGame(gameId, (User)session.getAttribute("currentUser"));
        }
        session.setAttribute("questionCounter", 0);
        session.setAttribute("scoreCounter", 0);
        session.setAttribute("questions", questions);
        model.addAttribute("currentQuestion", questions.get((int)session.getAttribute("questionCounter")));
        model.addAttribute("user", session.getAttribute("currentUser"));
        model.addAttribute("currentQuestionNumber", 1);
        model.addAttribute("totalNumberOfQuestions", questions.size());
        return "game";
    }

    @GetMapping("/select")
    public String getSelectionScreen(HttpSession session, Model model) {
        if(session.getAttribute("currentUser") == null){
            return ("redirect:/login");
        }
        model.addAttribute("categories", service.getQuizCategories());
        return "gameSelection";
    }

    @PostMapping("/select")
    public String postSelectionScreen(@RequestParam(required = false) String category, @RequestParam(required = false) Integer limit, HttpSession session, Model model) {
        session.setAttribute("limit", limit);
        if (limit != null) return "redirect:/game";

        session.setAttribute("category", category);
        model.addAttribute("limit", service.getQuizLimits());
        model.addAttribute("chosenCategory", service.getFancyCategoryString(category));
        return "gameSelection";
    }


    @PostMapping("/nextQuestion")
    public String nextQuestion(HttpSession session, Model model, @RequestParam(required = false) Integer answer) {
        int ctr = (int)session.getAttribute("questionCounter");
        List<Question> questionList = (List<Question>)session.getAttribute("questions");

        if (answer != null) {
            if (questionList.get(ctr).getMixedAnswers().get(answer).equals(questionList.get(ctr).getCorrectAnswer())) {
                session.setAttribute("scoreCounter",(Integer)session.getAttribute("scoreCounter")+100);

                System.out.println("Correct!");
            }
        }
        if(ctr == questionList.size()-1) {
            service.saveScore(new HighScore(null, (int)session.getAttribute("scoreCounter"), LocalDate.now(), (User)session.getAttribute("currentUser")));
            return "redirect:/score";
        }
            ctr++;
            session.setAttribute("questionCounter", ctr);
            model.addAttribute("currentQuestion", questionList.get(ctr));
            model.addAttribute("user", session.getAttribute("currentUser"));
            model.addAttribute("currentQuestionNumber", ctr+1);
            model.addAttribute("totalNumberOfQuestions", questionList.size());
            return "game";

    }

}
       /*

       User user = (User) session.getAttribute("currentUser");
        if(user == null){
            return ("redirect:/login");
        }


        */

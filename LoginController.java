package com.example.SchoolSpring.control;

import com.example.SchoolSpring.model.Student;
import com.example.SchoolSpring.service.AuthenticationService;
import com.example.SchoolSpring.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    private DatabaseService databaseService;
    public static AuthenticationService authenticationService;
    @Autowired
    public LoginController(DatabaseService databaseService , AuthenticationService authenticationService){
        this.databaseService = databaseService;
        this.authenticationService = authenticationService;

    }

    @GetMapping("/")
    public String toLogin(){
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model){
        if(authenticationService.isAuthenticated())
            return "success-login";
        else {
            model.addAttribute("student" , new Student());
            return "login";
        }
    }

    @PostMapping("/login")
    public String loginValidation(@ModelAttribute Student student) {
        if (!databaseService.isStudentRegistered(student.getId()) ||
            !databaseService.isValidPassword(student.getId(), student.getPassword())) {
            return "fail-login";
        }
        authenticationService.setAuthenticated(true);
        authenticationService.setAuthenticatedId(student.getId());
        return "redirect:/home";
    }

}

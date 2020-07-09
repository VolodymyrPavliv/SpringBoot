package com.mushroom.sweater.controller;

import com.mushroom.sweater.models.Role;
import com.mushroom.sweater.models.User;
import com.mushroom.sweater.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("title", "Registration");
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model){
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if(userFromDB != null){
            model.addAttribute("message", "Users exists!");
            return "registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);

        return "redirect:/login";
    }
}

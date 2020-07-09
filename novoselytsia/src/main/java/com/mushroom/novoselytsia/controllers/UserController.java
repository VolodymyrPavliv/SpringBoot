package com.mushroom.novoselytsia.controllers;

import com.mushroom.novoselytsia.models.Role;
import com.mushroom.novoselytsia.models.User;
import com.mushroom.novoselytsia.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/user")
    public String userList(Model model){
        model.addAttribute("title", "Users");
        model.addAttribute("users", userRepository.findAll());
        return "userList";
    }

    @GetMapping("user/{id}")
    public String editUser(
            @PathVariable(value = "id") Long id,
            Model model){
        model.addAttribute("title", "User editor");
        if(!userRepository.existsById(id)){
            return "redirect:/user";
        }
        Optional<User> user = userRepository.findById(id);
        ArrayList<User> u = new ArrayList<>();
        user.ifPresent(u::add);
        model.addAttribute("user", u);
        model.addAttribute("roles", Role.values());
        return "user_edit";
    }

    @PostMapping("/user")
    public String userSave(@RequestParam(value = "id") Long id,
                           @RequestParam Map<String, String> form,
                           @RequestParam String username,
                           Model model) {
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(username);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()){
            if(roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
        return "redirect:/user";
    }
}

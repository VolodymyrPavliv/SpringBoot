package com.mushroom.novoselytsia.controllers;

import com.mushroom.novoselytsia.models.Post;
import com.mushroom.novoselytsia.models.User;
import com.mushroom.novoselytsia.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Controller
public class NewsController {
    @Autowired
    private PostRepository postRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/news")
    public String news(@RequestParam(required = false, defaultValue = "") String filter, Model model){
        model.addAttribute("title", "News");
        Iterable<Post> posts = postRepository.findAll();
        if(filter == null || filter.isEmpty()){
            posts = postRepository.findAll();
        } else {
            posts = postRepository.findByAnons(filter);
        }
        model.addAttribute("posts", posts);
        model.addAttribute("filer", filter);
        return "news";
    }

    @GetMapping("/news/add")
    public String newsAdd(Model model){
        model.addAttribute("title", "Adding Post");
        return "news_add";
    }

    @PostMapping("/news/add")
    public String newsPost(
            @AuthenticationPrincipal User user,
            @RequestParam String title, @RequestParam String anons, @RequestParam String full_text,
            @RequestParam("file") MultipartFile file, Model model) throws IOException {

        Post post = new Post(title, anons , full_text, user);
        if(file != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuid = UUID.randomUUID().toString();
            String resultFileName = uuid+"."+file.getOriginalFilename();
            file.transferTo(new File(uploadPath+"/"+resultFileName));
            post.setFilename(resultFileName);
        }
        postRepository.save(post);
        return "redirect:/news";
    }

    @GetMapping("/news/{id}")
    public String newsDetails(@PathVariable(value = "id") long id, Model model){
        model.addAttribute("title", "Details");
        if(!postRepository.existsById(id)){
            return "redirect:/news";
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> p = new ArrayList<>();
        post.ifPresent(p::add);
        model.addAttribute("post",p);
        return "news_details";
    }

    @GetMapping("/news/{id}/edit")
    public String newsEdit(@PathVariable(value = "id") long id, Model model){
        model.addAttribute("title", "Edit");
        if(!postRepository.existsById(id)){
            return "redirect:/news";
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> p = new ArrayList<>();
        post.ifPresent(p::add);
        model.addAttribute("post",p);
        return "news_edit";
    }


    @PostMapping("/news/{id}/edit")
    public String newsPostEdit(@PathVariable(value = "id") long id, @RequestParam String title, @RequestParam String anons, @RequestParam String full_text,
                           Model model){
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postRepository.save(post);
        return "redirect:/news";
    }

    @GetMapping("/news/{id}/delete")
    public String newsDelete(@PathVariable(value = "id") long id,
                               Model model){
        postRepository.deleteById(id);
        return "redirect:/news";
    }
}

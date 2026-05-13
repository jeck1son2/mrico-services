package com.demo.security.web;

import com.demo.security.entity.User;
import com.demo.security.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        User u = userService.getUserById(id);
        return u;
    }

    @GetMapping("/test/{id}")
    public Object getUserById(Principal principal) {
        Authentication au = (Authentication) principal;
        Authentication bu = SecurityContextHolder.getContext().getAuthentication();
        return au;
    }

    @PostMapping
    public int insertUser(@RequestBody User u){
        return userService.insertUser(u);
    }



    public UserController(UserService userService){
        this.userService = userService;
    }
}

package com.stream.app.controllers;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stream.app.entities.User;
import com.stream.app.forms.LoginForm;
import com.stream.app.forms.UserForm;
import com.stream.app.playload.CustomMessage;
import com.stream.app.services.JWTService;
import com.stream.app.services.SecurityCustomUserDetailService;
import com.stream.app.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@ComponentScan
@CrossOrigin("*")
public class UserController {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JWTService jwtService;
    private SecurityCustomUserDetailService userDetailService;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JWTService jwtService, SecurityCustomUserDetailService userDetailService){
        this.userService=userService;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
        this.userDetailService=userDetailService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserForm form){
        try{
            User user=new User();
            user.setName(form.getName());
            user.setEmail(form.getEmail());
            user.setPassword(form.getPassword());
            if(userService.userExist(form.getEmail())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    CustomMessage.builder().Message("User already Exist!").success(false).build()
                );
            }

            userService.saveUser(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                CustomMessage.builder().Message("User registered successfully!").success(true).build()
            );
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                CustomMessage.builder().Message("Error during registration!").success(false).build()
            );
        }
           
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginForm loginForm){
        UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword());
        try{
            // Authentication authentication=authenticationManager.authenticate(
            //     new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword())
            // );
            // SecurityContextHolder.getContext().setAuthentication(authentication);
            authenticationManager.authenticate(authentication);

            UserDetails userDetails=userDetailService.loadUserByUsername(loginForm.getEmail());
            String token=jwtService.generateToken(userDetails);

            HttpHeaders headers=new HttpHeaders();
            headers.set("Authorization","Bearer "+token);

            return ResponseEntity.ok().headers(headers).body(
                CustomMessage.builder().Message("Login successful").success(true).build()
            );
        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                CustomMessage.builder().Message("Invalid Credentials!").success(false).build()
            );
        }
    }
}

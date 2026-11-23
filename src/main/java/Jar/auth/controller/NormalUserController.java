package Jar.auth.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.*;

import Jar.auth.service.NormalUserService;
import Jar.auth.dto.LoginRequest;
import Jar.auth.dto.UserProfileResponse;
import Jar.auth.service.AuthenticationService;
import Jar.auth.dto.RegisterRequest;

@RestController
public class NormalUserController {
    private final NormalUserService normalUserService;
    private final AuthenticationService authenticationService;

    public NormalUserController(NormalUserService normalUSerService,
                                AuthenticationService authenticationService
    ){
        this.normalUserService = normalUSerService;
        this.authenticationService = authenticationService;
    }


    @PostMapping("/user/register")//register users
    @ResponseStatus(HttpStatus.CREATED)
    public String registerUser(@RequestBody RegisterRequest user){
        normalUserService.register(user.getUsername(), user.getPassword(), user.getReenteredPassword());
        return "User registered successfully";
    }


    @PostMapping("/user/login")
    @ResponseStatus(HttpStatus.OK)
    public void loginUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) throws Exception{
        authenticationService.login(loginRequest, request, response);
    }


    //use to check for current user
    @GetMapping("/user/profile")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponse getCurrentUserProfile(){
        return authenticationService.userProfile();
    }



}

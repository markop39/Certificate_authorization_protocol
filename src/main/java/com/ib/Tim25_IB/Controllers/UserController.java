package com.ib.Tim25_IB.Controllers;

import com.ib.Tim25_IB.DTOs.*;
import com.ib.Tim25_IB.Repository.UserRepository;
import com.ib.Tim25_IB.model.User;
import com.ib.Tim25_IB.services.UserService;
import com.ib.Tim25_IB.services.email.EmailDetails;
import com.ib.Tim25_IB.services.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Random;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;
    //REGISTER A NEW USER
    @PostMapping(value="/register",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDTO request) throws IOException{
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
        userService.createUser(request, randomNumber);

        String url = "http://localhost:4200/activate";

        String body = "Hello,\n"
                + "Before you can use your certificate service account\n"
                + "you need to activate it using the activation code bellow.\n"
                + "\n" + randomNumber + "\n\n"
                + "\nEnter the activation code at the following page: \n"
                + "\n  " + url;

        String subject = "Account activation request";
        EmailDetails details = new EmailDetails(request.getEmail(), body, subject);
        emailService.sendSimpleMail(details);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/recover", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendRecovery(@RequestBody EmailDTO email){
        User user = userService.findByEmail(email.getEmail());
        if(user != null && user.isActivated()){
            Random random = new Random();
            int randomNumber = random.nextInt(900000) + 100000;
            user.setCode(randomNumber);
            userRepository.save(user); //TODO za finalnu odbranu prebaciti u servis da ne koristimo repo u kontroleru
            userRepository.flush();
            String url = "http://localhost:4200/password";

            String body = "Hello,\n"
                    + "You requested a password recovery.\n"
                    + "Follow the url bellow and enter the code:\n"
                    + "\n" + randomNumber + "\n\n"
                    + "\n and your new password. \n"
                    + "\n  Password reset page: " + url;

            String subject = "Password recovery request";
            EmailDetails details = new EmailDetails(user.getEmail(), body, subject);
            emailService.sendSimpleMail(details);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping(value="/recover", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> recoverPassword(@RequestBody NewPasswordDTO request){
        boolean success = userService.recoverPassword(request);
        if(success){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/activate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> activateAccount(@RequestBody ActivationDTO activation){
        boolean activated = userService.activateAccount(activation);
        if(activated){
            return new ResponseEntity<>(HttpStatus.OK);

        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value="/register/admin",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerAdmin(@RequestBody UserRequestDTO request) throws IOException {

        userService.createAdmin(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //LOG IN AN EXISTING USER
    @PostMapping(value="/login",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequestDTO request) throws IOException {
        boolean check = userService.loginUser(request);
        //check active
        if (check)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value="/login/auth",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginUserAuth(@RequestBody UserLoginRequestAuthDTO request) throws IOException {

        boolean check = userService.loginUserAuth(request);
        //check active
        if (check)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/login/send", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendLoginAuth(@RequestBody EmailDTO email){
        User user = userService.findByEmail(email.getEmail());
        if(user != null && user.isActivated() && user.getCode()==0){
            Random random = new Random();
            int randomNumber = random.nextInt(900000) + 100000;
            user.setCode(randomNumber);
            userRepository.save(user); //TODO za finalnu odbranu prebaciti u servis da ne koristimo repo u kontroleru
            userRepository.flush();
            String url = "http://localhost:4200/password";

            String body = "Hello,\n"
                    + "Your login code is: \n"
                    + "\n" + randomNumber + "\n\n";

            String subject = "Login 2 factor authentication";
            EmailDetails details = new EmailDetails(user.getEmail(), body, subject);
            emailService.sendSimpleMail(details);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

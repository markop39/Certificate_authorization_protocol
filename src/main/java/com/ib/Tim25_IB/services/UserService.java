package com.ib.Tim25_IB.services;

import com.ib.Tim25_IB.DTOs.*;
import com.ib.Tim25_IB.Repository.UserRepository;
import com.ib.Tim25_IB.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
//    public BCryptPasswordEncoder passwordEncoderUser() {
//        return new BCryptPasswordEncoder();
//    }
    public void createUser(UserRequestDTO requestDTO, int code) throws IOException {
        User user = new User(requestDTO);
        user.setActivated(false);
        user.setCode(code);
//        user.setPassword(passwordEncoderUser().encode(user.getPassword()));
        userRepository.save(user);
        userRepository.flush();
    }

    public void createAdmin(UserRequestDTO requestDTO) throws IOException {
        User user = new User(requestDTO);
        user.setAdmin(true);
        userRepository.save(user);
        userRepository.flush();
    }

    public boolean loginUser(UserLoginRequestDTO requestDTO) throws IOException {
        Optional<User> found = Optional.ofNullable(userRepository.findByEmail(requestDTO.getEmail()));

        if(found.isPresent() && found.get().getPassword().equals(requestDTO.getPassword())){
            if(found.get().isActivated()){
                return true;
            }else{
                return  false;
            }
        }else{
            return false;
        }
    }

    public User findByEmail(String email){
        Optional<User> found = Optional.ofNullable(userRepository.findByEmail(email));
        return found.orElse(null);
    }
    
    public boolean isUserAdmin(String email){
        return userRepository.findByEmail(email).isAdmin();
    }

    public void getRequests() {
    }

    public void processCertificate() {
    }

    public boolean activateAccount(ActivationDTO activation) {
        Optional<User> found = Optional.ofNullable(userRepository.findByEmail(activation.getEmail()));
        if(found.isPresent() && found.get().getCode() == activation.getCode()){
            User user = found.get();
            user.setActivated(true);
            user.setCode(0);
            userRepository.save(user);
            userRepository.flush();
            return true;
        }else{
            return false;
        }
    }

    public boolean recoverPassword(NewPasswordDTO request) {
        User user  = findByEmail(request.getEmail());
        if(user == null  || !request.getNewPassword().equals(request.getPasswordConfirmation())){
            return false;
        }else{
            user.setPassword(request.getNewPassword());
            userRepository.save(user);
            userRepository.flush();
            return true;
        }


    }

    public boolean loginUserAuth(UserLoginRequestAuthDTO request) {
        Optional<User> found = Optional.ofNullable(userRepository.findByEmail(request.getEmail()));

        if(found.isPresent() && found.get().getPassword().equals(request.getPassword())){
            if(found.get().isActivated() && found.get().getCode() == request.getCode()){
                found.get().setCode(0);
                userRepository.save(found.get());
                return true;
            }else{
                return  false;
            }
        }else{
            return false;
        }
    }
}

package com.ib.Tim25_IB.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ib.Tim25_IB.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    List<User> findAll();



//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public List<User> getAllUsers() throws IOException {
//        File file = new File("users.json");
//        if (!file.exists()) {
//            return new ArrayList<>();
//        }
//        return objectMapper.readValue(file, new TypeReference<List<User>>() {});
//    }
//
//    public void save(List<User> users) throws IOException {
//        File file = new File("users.json");
//        objectMapper.writeValue(file, users);
//    }
//
//    public User findByUsername(String username) throws IOException{
//        List<User> users = getAllUsers();
//        for (User user: users) {
//            if (user.getEmail().equals(username)){
//                return user;
//            }
//        }
//        return null;
//    }
}

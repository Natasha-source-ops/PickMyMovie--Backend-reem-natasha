package htw.webtech.pickmymovie.controller;

import htw.webtech.pickmymovie.model.User;
import htw.webtech.pickmymovie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody User user) {
        String login = user.getUsername();

        if (login == null || login.isBlank()) {
            login = user.getEmail();
        }

        Optional<User> foundUser = userService.findUserForLogin(login);

        if (foundUser.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        boolean passwordCorrect = userService.passwordMatches(foundUser.get(), user.getPassword());

        if (!passwordCorrect) {
            return ResponseEntity.status(401).body("Wrong password");
        }

        return ResponseEntity.ok(foundUser.get());
    }
}

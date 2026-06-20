package htw.webtech.pickmymovie.service;

import htw.webtech.pickmymovie.Repository.UserRepository;
import htw.webtech.pickmymovie.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WatchlistService watchlistService;

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUserById(Long id) {
        watchlistService.deleteWatchlistEntriesByUserId(id);
        userRepository.deleteById(id);
    }

    public Optional<User> findUserForLogin(String login) {
        return userRepository.findByUsernameOrEmail(login, login);
    }

    public boolean passwordMatches(User user, String password) {
        if (user == null || password == null || user.getPassword() == null) {
            return false;
        }

        try {
            return BCrypt.checkpw(password, user.getPassword());
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
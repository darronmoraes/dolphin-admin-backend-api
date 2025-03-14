package in.nineteen96.dolphin.service.db;

import in.nineteen96.dolphin.entity.User;
import in.nineteen96.dolphin.exception.UserNotFoundException;
import in.nineteen96.dolphin.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDbService, UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public Optional<User> findUserByID(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User findByEmail(String email) {
        return findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("user %s not found", email)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username).orElseThrow(() -> new UserNotFoundException(String.format("user %s not found", username)));
    }
}

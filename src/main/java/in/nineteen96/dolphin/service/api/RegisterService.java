package in.nineteen96.dolphin.service.api;

import in.nineteen96.dolphin.entity.User;
import in.nineteen96.dolphin.exception.UserAlreadyExistsException;
import in.nineteen96.dolphin.service.db.UserService;
import in.nineteen96.dolphin.service.dto.input.RegisterInput;
import in.nineteen96.dolphin.service.dto.output.RegisterOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class RegisterService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public RegisterOutput processRegister(RegisterInput input) {
        log.info("attempt to register user {}, type {}", input.getEmail(), input.getType());

        Optional<User> optionalUser = userService.findUserByEmail(input.getEmail());

        if (optionalUser.isPresent()) {
            log.warn("user {} already present in system", input.getEmail());
            throw new UserAlreadyExistsException(String.format("user %s already exists", input.getEmail()));
        }

        log.info("setting user entity...");
        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        String encodedPassword = passwordEncoder.encode(input.getPassword());
        user.setPassword(encodedPassword);
        user.setType(input.getType());
        user.setDeleted(false);
        user.setStatus(true);

        user = userService.save(user);

        log.info("setting output...");
        RegisterOutput output = RegisterOutput.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .type(user.getType())
                .active(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();

        log.info("completed registration service");
        return output;
    }

}

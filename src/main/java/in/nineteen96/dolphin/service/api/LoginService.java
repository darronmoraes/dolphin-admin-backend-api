package in.nineteen96.dolphin.service.api;

import in.nineteen96.dolphin.auth.JwtTokenProvider;
import in.nineteen96.dolphin.service.db.UserService;
import in.nineteen96.dolphin.service.dto.input.LoginInput;
import in.nineteen96.dolphin.service.dto.output.LoginOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginOutput processLogin(LoginInput input) {
        log.info("attempt to login, login service invoked");

        String email = input.getEmail();
        String password = input.getPassword();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        log.info("user authentication complete. setting context...");

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication auth = new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials(), authentication.getAuthorities());
        //auth.setAuthenticated(true);
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        final String token = jwtTokenProvider.generateToken(auth);

        log.info("token generated successfully, completed service");
        return LoginOutput.builder()
                .token(token)
                .build();
    }
}

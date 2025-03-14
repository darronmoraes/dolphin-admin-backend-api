package in.nineteen96.dolphin.service.business;

import in.nineteen96.dolphin.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class DolphinSystemAbstract {

    public String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return user.getEmail();
        }
        throw new RuntimeException("user is not authenticated");
    }

}

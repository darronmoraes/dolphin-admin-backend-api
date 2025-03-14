package in.nineteen96.dolphin.auth;

import in.nineteen96.dolphin.exception.AuthorizationHeaderException;
import in.nineteen96.dolphin.service.db.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtTokenProvider tokenProvider;

    private final UserService userService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        logger.info("authorizing user");

        String requestURI = req.getRequestURI();
        logger.info("invoked endpoint: {}", requestURI);

        logger.info("getting authorization header");
        final String authorizationHeader = req.getHeader("Authorization");

        try {
            final String token;

            /*if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new AuthorizationHeaderException("Authorization header not found or invalid");
            }*/
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                final String userEmail;

                userEmail = tokenProvider.extractUserEmail(token);
                // todo extract the user email from jwt token

                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    logger.debug("username found in token and security context is null...");
                    logger.debug("loading user by username");
                    UserDetails userDetails = this.userService.loadUserByUsername(userEmail);
                    logger.info("validating token...");
                    if (tokenProvider.isTokenValid(token, userDetails)) {
                        logger.info("token is valid");
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        logger.info("security context updated");
                    }
                }
            } else {
                logger.warn("couldn't find bearer string, ignoring header for URI {}", req.getRequestURL());
            }

            filterChain.doFilter(req, res);
            SecurityContextHolder.getContext().setAuthentication(null);
            logger.info("cleared context");
        } catch (AuthorizationHeaderException e) {
            logger.warn("authorization header {}", e.getLocalizedMessage(), e);
            req.setAttribute("authorization-header", e);
            req.getRequestDispatcher("/failed/token-error?error=" + e.getLocalizedMessage()).forward(req, res);
        } catch (ExpiredJwtException e) {
            logger.warn("expired jwt exception {}", e.getLocalizedMessage(), e);
            req.setAttribute("expired-token", e);
            req.getRequestDispatcher("/failed/token-error?error=" + e.getLocalizedMessage()).forward(req, res);
        } catch (Exception e) {
            logger.warn("authentication filter: {}", e.getLocalizedMessage(), e);
            req.getRequestDispatcher("/failed/token-error?error=" + e.getLocalizedMessage()).forward(req, res);
        }

    }
}

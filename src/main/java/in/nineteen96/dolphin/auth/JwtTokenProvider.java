package in.nineteen96.dolphin.auth;

import in.nineteen96.dolphin.entity.User;
import in.nineteen96.dolphin.service.db.UserService;
import in.nineteen96.dolphin.util.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${jwt.token.secret}")
    private String SECRET_KEY;

    @Autowired
    private UserService userService;

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(Authentication authentication) {
        logger.info("generating access token");
        //  todo: find user and set here
        String email = authentication.getPrincipal().toString();
        logger.info("token for auth principal {}", email);
        User existingUser = userService.findByEmail(email);

        String role = existingUser.getType().toString();

        return Jwts
                .builder()
                .setIssuer("dolphin system")
                .setSubject(existingUser.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constant.ONE_HOUR_DURATION_TEST))
                .claim("roles", role)
                .claim("name", existingUser.getFullName())
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Generic method to extract any claims in the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserEmail(token);

        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private Claims extractAllClaims(String token) {
        logger.info("invoked extract all claims");
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}

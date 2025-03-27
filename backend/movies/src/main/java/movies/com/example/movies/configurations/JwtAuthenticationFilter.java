package movies.com.example.movies.configurations;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import movies.com.example.movies.services.jwt.UserService;
import movies.com.example.movies.utils.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserService userService;

    public JwtAuthenticationFilter(JWTUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getServletPath();
        String authHeader = request.getHeader("Authorization");

        // Skip filtering for public auth endpoints
        if (requestPath.equals("/api/auth/login") || requestPath.equals("/api/auth/signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Defensive check for missing or malformed Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No Authorization header or not a Bearer token.");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7).trim();

        // If token is empty after Bearer
        if (token.isBlank()) {
            System.out.println("Bearer token is empty.");
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims;
        try {
            claims = jwtUtil.extractAllClaims(token);
            System.out.println("JWT extracted for user: " + claims.getSubject());
        } catch (Exception e) {
            System.out.println("Invalid JWT: " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        // Expiry check
        if (jwtUtil.isTokenExpired(token)) {
            System.out.println(" JWT expired for user: " + claims.getSubject());
            filterChain.doFilter(request, response);
            return;
        }

        String username = claims.getSubject();

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("Authenticated user: " + username);
                System.out.println("Authorities: " + userDetails.getAuthorities());
            } else {
                System.out.println("Token validation failed for user: " + username);
            }
        }

        filterChain.doFilter(request, response);
    }
}

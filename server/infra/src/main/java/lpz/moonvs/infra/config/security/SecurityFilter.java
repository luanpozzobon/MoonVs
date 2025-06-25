package lpz.moonvs.infra.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.auth.contracts.IUserRepository;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final IUserRepository repository;

    @Autowired
    public SecurityFilter(final TokenService tokenService,
                          final IUserRepository repository) {
        this.tokenService = tokenService;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String token = recoverToken(request);
        if (token != null) {
            final Id<User> id = Id.from(tokenService.validateToken(token));
            final Optional<User> userResult = repository.findById(id);

            if (userResult.isPresent()) {
                final User user = userResult.get();
                final UserDetails userDetails = new CustomUserDetails(
                        user.getId(), user.getUsername(), user.getPassword().getValue(), Collections.emptyList());

                final var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return null;

        for (final Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) return cookie.getValue();
        }

        return null;
    }
}

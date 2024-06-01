package pl.drivewheelsdeals.app.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.drivewheelsdeals.app.request.LoginRequest;
import pl.drivewheelsdeals.app.response.LoginResponse;
import pl.drivewheelsdeals.app.service.JwtService;

@RestController
public class AuthController {

    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) throws Exception {
        // Authenticate the user
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password", e);
        }
        // If authentication is successful, generate a JWT
        return new LoginResponse(jwtService.generateToken(request.getEmail()));
    }

}

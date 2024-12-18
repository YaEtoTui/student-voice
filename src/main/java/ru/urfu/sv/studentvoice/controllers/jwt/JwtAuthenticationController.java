package ru.urfu.sv.studentvoice.controllers.jwt;

import io.swagger.v3.oas.annotations.Operation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.model.domain.dto.user.UserClient;
import ru.urfu.sv.studentvoice.services.jwt.JwtUserDetailsService;
import ru.urfu.sv.studentvoice.utils.exceptions.InvalidLoginException;
import ru.urfu.sv.studentvoice.utils.jwt.JwtUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Контролер предназначен для авторизации, чтобы сгенерировать токен
 *
 * @author Sazhin Egor
 * @since 20.10.2024
 */
@RestController
@RequestMapping(Links.BASE_API)
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Авторизация")
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @SneakyThrows
    public ResponseEntity<Map<String, String>> login(@RequestBody UserClient user) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (AuthenticationException e) {
            throw new InvalidLoginException("Invalid login or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        final Map<String, String> response = new HashMap<>();
        response.put("token", jwt);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
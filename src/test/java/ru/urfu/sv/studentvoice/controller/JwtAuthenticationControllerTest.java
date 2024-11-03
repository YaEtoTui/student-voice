package ru.urfu.sv.studentvoice.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.urfu.sv.studentvoice.controllers.jwt.JwtAuthenticationController;
import ru.urfu.sv.studentvoice.model.domain.dto.auth.UserClient;
import ru.urfu.sv.studentvoice.services.jwt.JwtUserDetailsService;
import ru.urfu.sv.studentvoice.utils.jwt.JwtUtil;

import java.util.ArrayList;
import java.util.Map;

/**
 * В данной классе тестируем JwtAuthenticationController
 */
@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationControllerTest {

    /**
     * Manager проверяет, что пользователь авторизован или нет
     */
    @Mock
    private AuthenticationManager authenticationManager;

    /**
     * Сервис по работе с user
     */
    @Mock
    private JwtUserDetailsService userDetailsService;

    /**
     * Утилита для работы с токеном
     */
    @Mock
    private JwtUtil jwtUtil;

    /**
     * Контролер предназначен для авторизации, чтобы сгенерировать токен
     */
    @InjectMocks
    private JwtAuthenticationController jwtAuthenticationController;

    /**
     * Используем для инициализации моков перед каждым тестом
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * В данной методе проверяем метод login() на успешную авторизацию
     */
    @Test
    public void testLoginSuccess() {

        final String username = "testUser";
        final String password = "testPassword";

        final UserClient user = new UserClient();
        user.setUsername(username);
        user.setPassword(password);

        final UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, password, new ArrayList<>());
        final String jwtToken = "testJwtToken";

        /* Создаем мок-объект для authenticationManager */
        /* В данном случае он проверяет есть ли данный пользователь. Ничего не возвращает(то есть существует данный пользователь), /*
        /* если user авторизован */
        Mockito.when(authenticationManager.authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        /* Проверяем, что данный user существует */
        /* Возвращаем самого user */
        Mockito.when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(userDetails);

        /* Генерируем токен */
        Mockito.when(jwtUtil.generateToken(userDetails)).thenReturn(jwtToken);

        /* Авторизуемся через запрос login() */
        /* Уже сама проверка. Выше были приготовления */
        final ResponseEntity<Map<String, String>> response = jwtAuthenticationController.login(user);

        /* Ниже проверки: на статус кода и на значение токена */
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(jwtToken, response.getBody().get("token"));
    }

    /**
     * В данной методе проверяем метод login(),
     * когда пользователь не смог авторизоваться
     */
    @Test
    public void testLoginFailure() {

        final String username = "testUser";
        final String password = "testPassword";

        final UserClient user = new UserClient();
        user.setUsername(username);
        user.setPassword(password);

        /* Создаем мок-объект для authenticationManager */
        /* В данном случае он проверяет есть ли данный пользователь. Кидает exception, если user не смог авторизоваться */
        Mockito.when(authenticationManager.authenticate(ArgumentMatchers.any(UsernamePasswordAuthenticationToken.class))).thenThrow(new UsernameNotFoundException("User not found"));

        // Проверка на получении ошибки
        Assertions.assertThrows(Exception.class, () -> jwtAuthenticationController.login(user));
    }
}
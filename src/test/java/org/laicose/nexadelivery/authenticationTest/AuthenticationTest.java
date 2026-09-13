package org.laicose.nexadelivery.authenticationTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laicose.nexadelivery.Enum.DriverStatus;
import org.laicose.nexadelivery.auth.AuthenticationController;
import org.laicose.nexadelivery.dto.request.DriverRegister;
import org.laicose.nexadelivery.repository.DriverRepository;
import org.laicose.nexadelivery.repository.MerchantRepository;
import org.laicose.nexadelivery.repository.UserRepository;
import org.laicose.nexadelivery.configuration.JwtUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationController authenticationController;


    @Test
    void registerDriverEmailIsAvailableTest() {

        DriverRegister request = new DriverRegister();
        request.setName("Zakaria");
        request.setEmail("driver@nexadelivery.com");
        request.setTelephone("0600000000");
        request.setPassword("123456");

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("encodedPassword");

        ResponseEntity<?> response =
                authenticationController.registerDriver(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(
                "Driver registered successfully",
                response.getBody()
        );

        verify(userRepository)
                .existsByEmail(request.getEmail());

        verify(passwordEncoder)
                .encode(request.getPassword());

        verify(driverRepository)
                .save(argThat(driver ->
                        driver.getEmail().equals("driver@nexadelivery.com")
                                && driver.getDriverStatus() == DriverStatus.DISPONIBLE
                                && driver.getAverageRating() == 0.0
                ));
    }


    @Test
    void registerDriverEmailAlreadyExistsTest() {

        DriverRegister request = new DriverRegister();
        request.setName("Zakaria");
        request.setEmail("driver@nexadelivery.com");
        request.setTelephone("0600000000");
        request.setPassword("123456");

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authenticationController.registerDriver(request)
        );

        assertEquals(
                "Cet email est déjà utilisé",
                exception.getMessage()
        );

        verify(userRepository)
                .existsByEmail(request.getEmail());

        verify(driverRepository, never())
                .save(any());

    }
}

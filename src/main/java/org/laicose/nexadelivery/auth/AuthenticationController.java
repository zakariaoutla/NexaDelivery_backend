package org.laicose.nexadelivery.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.Enum.DriverStatus;
import org.laicose.nexadelivery.Enum.Role;
import org.laicose.nexadelivery.configuration.JwtUtil;
import org.laicose.nexadelivery.dto.request.MerchantRegister;
import org.laicose.nexadelivery.dto.request.UserLogin;
import org.laicose.nexadelivery.dto.request.DriverRegister;
import org.laicose.nexadelivery.model.Driver;
import org.laicose.nexadelivery.model.Merchant;
import org.laicose.nexadelivery.repository.DriverRepository;
import org.laicose.nexadelivery.repository.MerchantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final DriverRepository driverRepository;
    private final MerchantRepository merchantRepository;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLogin request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()));
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(Map.of("token", token));
    }


    @PostMapping("/register/driver")
    public ResponseEntity<?> registerDriver(
            @RequestBody @Valid DriverRegister request) {

        Driver driver = new Driver();

        driver.setEmail(request.getEmail());
        driver.setName(request.getName());
        driver.setTelephone(request.getTelephone());
        driver.setPassword(passwordEncoder.encode(request.getPassword()));

        driver.setRole(Role.DRIVER);
        driver.setCreatedAt(LocalDateTime.now());
        driver.setDriverStatus(DriverStatus.DISPONIBLE);
        driver.setAverageRating(0.0);

        driverRepository.save(driver);

        return ResponseEntity.ok("Driver registered successfully");
    }

    @PostMapping("/register/merchant")
    public ResponseEntity<?> registerMerchant(@RequestBody @Valid MerchantRegister request) {
        Merchant merchant = new Merchant();
        merchant.setEmail(request.getEmail());
        merchant.setName(request.getName());
        merchant.setTelephone(request.getTelephone());
        merchant.setPassword(passwordEncoder.encode(request.getPassword()));
        merchant.setRole(Role.MERCHANT);
        merchant.setCreatedAt(LocalDateTime.now());
        merchant.setCollectionAddress(request.getCollectionAddress());
        merchantRepository.save(merchant);

        return ResponseEntity.ok("User registered successfully");
    }


}

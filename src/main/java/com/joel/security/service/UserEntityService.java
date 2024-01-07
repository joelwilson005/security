package com.joel.security.service;

import com.joel.security.config.security.JWTTokenService;
import com.joel.security.controller.request.LoginShopperRequest;
import com.joel.security.controller.request.RegisterShopperRequest;
import com.joel.security.model.Role;
import com.joel.security.model.RoleType;
import com.joel.security.model.Shopper;
import com.joel.security.repository.RoleRepository;
import com.joel.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UserEntityService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenService jwtTokenService;

    @Autowired
    public UserEntityService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, @Lazy AuthenticationManager authenticationManager, JWTTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findUserEntityByEmail(username).orElseThrow();
    }


    public HashMap<String, Object> authenticateShopper(String email, String password) {
        Shopper shopper = (Shopper) this.userRepository.findUserEntityByEmail(email).orElseThrow();
        var authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        String jwt = this.jwtTokenService.generateJwt(authentication, shopper.getId());
        var shopperAndJwt = new HashMap<String, Object>();
        shopperAndJwt.put("shopper", shopper);
        shopperAndJwt.put("jwt", jwt);
        return shopperAndJwt;
    }

    public HashMap<String, Object> registerShopper(RegisterShopperRequest registerShopperRequest) throws Exception {
        if (this.userRepository.findUserEntityByEmail(registerShopperRequest.email()).isPresent()) {

            throw new Exception("Email address is already present");
        }
        var shopper = new Shopper();
        shopper.setEmail(registerShopperRequest.email());
        shopper.setPassword(registerShopperRequest.password());
        var shopperRole = roleRepository.findByAuthority(RoleType.SHOPPER.name()).orElseThrow();
        Set<Role> authorities = new HashSet<>();
        authorities.add(shopperRole);
        shopper.setAuthorities(authorities);

        final String unencodedPassword = shopper.getPassword();
        shopper.setPassword(this.passwordEncoder.encode(registerShopperRequest.password()));

        this.userRepository.save(shopper);
        return this.authenticateShopper(shopper.getEmail(), unencodedPassword);
    }


    public HashMap<String, Object> loginShopper(LoginShopperRequest request) {
        return this.authenticateShopper(request.email(), request.password());
    }
}

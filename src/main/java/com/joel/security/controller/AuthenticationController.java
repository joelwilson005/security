package com.joel.security.controller;

import com.joel.security.controller.request.AuthenticatedShopperResponse;
import com.joel.security.controller.request.LoginShopperRequest;
import com.joel.security.controller.request.RegisterShopperRequest;
import com.joel.security.model.Shopper;
import com.joel.security.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/v1/auth"})
public class AuthenticationController {

    private final UserEntityService userEntityService;

    @Autowired
    public AuthenticationController(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }

    @PostMapping(value = "/shopper/register")
    public ResponseEntity<AuthenticatedShopperResponse> registerShopper(@RequestBody RegisterShopperRequest request) throws Exception {
        var authenticatedShopper = this.userEntityService.registerShopper(request);
        Shopper shopper = (Shopper) authenticatedShopper.get("shopper");
        String jwt = (String) authenticatedShopper.get("jwt");
        return new ResponseEntity<>(new AuthenticatedShopperResponse(shopper.getId().toString(), shopper.getEmail(), jwt), HttpStatus.CREATED);

    }

    @PostMapping(value = "/shopper/login")
    public ResponseEntity<AuthenticatedShopperResponse> loginShopper(@RequestBody LoginShopperRequest request) {
        var authenticatedShopper = this.userEntityService.loginShopper(request);
        Shopper shopper = (Shopper) authenticatedShopper.get("shopper");
        String jwt = (String) authenticatedShopper.get("jwt");
        return new ResponseEntity<>(new AuthenticatedShopperResponse(shopper.getId().toString(), shopper.getEmail(), jwt), HttpStatus.OK);

    }
}

package com.joel.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/v1"})
public class ShopperController {

    @GetMapping("/shoppers/{idInPath}/cart")
    @PreAuthorize("hasAuthority('ROLE_SHOPPER') and authentication.principal.claims['id'] == #idInPath")
    public ResponseEntity<String> getCart(@PathVariable String idInPath) {
        String message = "Cart belongs to user with ID: " + idInPath;
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}

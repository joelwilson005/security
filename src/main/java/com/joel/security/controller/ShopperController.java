package com.joel.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping({"/api/v1"})
public class ShopperController {

    @GetMapping("/shoppers/{id}/cart")
    @PreAuthorize("hasAuthority('ROLE_SHOPPER')")
    public ResponseEntity<String> getCart(@PathVariable String id) {
        String message = "Cart belongs to user with ID: " + id;
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}

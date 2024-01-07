package com.joel.security.controller.request;

import jakarta.validation.constraints.Email;

public record RegisterShopperRequest(@Email String email, String password) {}

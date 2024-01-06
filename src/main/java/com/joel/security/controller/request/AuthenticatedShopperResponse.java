package com.joel.security.controller.request;

public record AuthenticatedShopperResponse(String id, String email, String token) {}

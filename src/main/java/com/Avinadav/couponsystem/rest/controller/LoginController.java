package com.Avinadav.couponsystem.rest.controller;

import com.Avinadav.couponsystem.rest.ex.InvalidLoginException;
import com.Avinadav.couponsystem.rest.login.CS_System;
import com.Avinadav.couponsystem.rest.login.ClientSession;
import com.Avinadav.couponsystem.rest.models.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class LoginController {

    public static final int ADMIN_TOKEN_LENGTH = 15;
    public static final int COMPANY_TOKEN_LENGTH = 10;
    public static final int CUSTOMER_TOKEN_LENGTH = 5;

    private final Map<String, ClientSession> tokensMap;
    private final CS_System csSystem;

    @Autowired
    public LoginController(@Qualifier("tokens") Map<String, ClientSession> tokensMap, CS_System csSystem) {
        this.tokensMap = tokensMap;
        this.csSystem = csSystem;
    }


    @PostMapping("/login")
    public ResponseEntity<Token> login(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String type)
            throws InvalidLoginException {

        ClientSession session = csSystem.createSession(email, password, type);

        Token token = new Token(generateToken(type));

        session.access();

        tokensMap.put(token.getToken(), session);
        return ResponseEntity.ok(token);

    }

    /**
     * This method generate token to all types of users (ADMIN/COMPANY/CUSTOMER).
     * Every type of user get different length of token to secure the system.
     *
     * @param type - Type of user (ADMIN/COMPANY/CUSTOMER).
     * @return - String of random token.
     * @throws InvalidLoginException - If there's a login problem
     */

    private String generateToken(String type) throws InvalidLoginException {
        String token = UUID.randomUUID()
                .toString()
                .replace("-", "");
        switch (type) {
            case "admin":
                return token.substring(0, ADMIN_TOKEN_LENGTH);
            case "company":
                return token.substring(0, COMPANY_TOKEN_LENGTH);
            case "customer":
                return token.substring(0, CUSTOMER_TOKEN_LENGTH);
            default:
                throw new InvalidLoginException("Login type is not supported!");
        }
    }
}



package com.se2.hanuairline.controller;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/oauth")
public class OauthController {

    @GetMapping("/callback")
    public ResponseEntity<?> get2Token(HttpServletRequest request) throws IOException {
        String test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println(test);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/callback")
    public ResponseEntity<?> getToken(HttpServletRequest request) throws IOException {
        String test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println(test);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

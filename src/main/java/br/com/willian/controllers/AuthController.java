package br.com.willian.controllers;

import br.com.willian.dtos.BooksDTO;
import br.com.willian.dtos.security.AccountCredentialsDTO;
import br.com.willian.dtos.security.TokenDTO;
import br.com.willian.services.AuthServices;
import br.com.willian.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Authentication EndPoint", description = "Endpoints For Authentication")
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthServices authServices;

    @SuppressWarnings("rawtypes")
    @PostMapping(value = "/signin")
    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Operation(summary = "Authenticates a user",
            description = "Authenticates a user and returns a token",
            tags = {"Authentication EndPoint"})
    public ResponseEntity singnin(@RequestBody AccountCredentialsDTO data) {
        if(checkIfParamsIsNotNull(data)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");

        var token = authServices.signin(data);

        if(token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }
        return token;
    }

    public boolean checkIfParamsIsNotNull(AccountCredentialsDTO data) {
        return data == null || data .getUserName() == null || data.getUserName().isBlank()
                || data.getPassword() == null || data.getPassword().isBlank();
    }

}

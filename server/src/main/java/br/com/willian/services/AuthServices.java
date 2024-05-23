package br.com.willian.services;

import br.com.willian.dtos.security.AccountCredentialsDTO;
import br.com.willian.dtos.security.TokenDTO;
import br.com.willian.model.User;
import br.com.willian.repositories.UserRepository;
import br.com.willian.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    public ResponseEntity signin(AccountCredentialsDTO data) {
        try {
            String userName = data.getUserName();
            String password = data.getPassword();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName,password));

            User user = repository.findByUserName(userName);

            TokenDTO tokenResponse;

            if (user != null) {
                tokenResponse = tokenProvider.createAccessToken(userName, user.getRoles());
            } else {
                throw new UsernameNotFoundException("UserName " + userName + " not found");
            }

            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid userName/password supplied!");
        }
    }

    public ResponseEntity refreshToken(String userName, String refreshToken) {

        User user = repository.findByUserName(userName);

        TokenDTO tokenResponse;

        if (user != null) {
            tokenResponse = tokenProvider.refreshAccessToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("UserName " + userName + " not found");
        }

        return ResponseEntity.ok(tokenResponse);
    }
}

package spring.security.jwt.springsecurity.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.security.jwt.springsecurity.Dto.RequestResponse;
import spring.security.jwt.springsecurity.Model.OurUsers;
import spring.security.jwt.springsecurity.Repository.UserRepository;

import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public RequestResponse signUp(RequestResponse registrationRequest) {
        RequestResponse requestResponse = new RequestResponse();
        try{
            OurUsers ourUsers = new OurUsers();
            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setRole(registrationRequest.getRole());
            OurUsers ourUsersResults = userRepository.save(ourUsers);
            if (ourUsersResults != null && ourUsersResults.getId() > 0) {
                requestResponse.setOurUsers(ourUsersResults);
                requestResponse.setMessage("User Saved Successfully");
                requestResponse.setStatusCode(200);
            }
        } catch (Exception e) {
            requestResponse.setStatusCode(500);
            requestResponse.setError(e.getMessage());
        }
        return requestResponse;
    }

    public RequestResponse signIn(RequestResponse signInRequest) {
        RequestResponse requestResponse = new RequestResponse();

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
            var user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow();
            System.out.println("User is: "+user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            requestResponse.setStatusCode(200);
            requestResponse.setToken(jwt);
            requestResponse.setRefreshToken(refreshToken);
            requestResponse.setExpirationTime("24Hrs");
            requestResponse.setMessage("Successfully Signed In");
        }catch (Exception e){
            requestResponse.setStatusCode(500);
            requestResponse.setError(e.getMessage());
        }
        return requestResponse;
    }

    public RequestResponse refreshToken(RequestResponse refreshTokenRequest) {
        RequestResponse requestResponse = new RequestResponse();
        String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
        OurUsers user = userRepository.findByEmail(ourEmail).orElseThrow();
        if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtUtils.generateToken(user);
            requestResponse.setStatusCode(200);
            requestResponse.setToken(jwt);
            requestResponse.setRefreshToken(refreshTokenRequest.getToken());
            requestResponse.setExpirationTime("24Hrs");
            requestResponse.setMessage("Successfully Refreshed Token");
        }
        requestResponse.setStatusCode(500);
        return requestResponse;
    }

}

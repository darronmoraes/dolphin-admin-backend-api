package in.nineteen96.dolphin.controller;

import in.nineteen96.dolphin.service.api.LoginService;
import in.nineteen96.dolphin.service.api.RegisterService;
import in.nineteen96.dolphin.service.dto.input.LoginInput;
import in.nineteen96.dolphin.service.dto.input.RegisterInput;
import in.nineteen96.dolphin.service.dto.output.LoginOutput;
import in.nineteen96.dolphin.service.dto.output.RegisterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RegisterService registerService;

    @PostMapping("/login")
    public ResponseEntity<LoginOutput> login(@RequestBody LoginInput input) {
        return new ResponseEntity<>(loginService.processLogin(input), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterOutput> register(@RequestBody RegisterInput input) {
        return new ResponseEntity<>(registerService.processRegister(input), HttpStatus.CREATED);
    }
}

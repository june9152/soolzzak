package com.example.zzan.user.controller;

import com.example.zzan.global.dto.ResponseDto;
import com.example.zzan.user.dto.UserLoginDto;
import com.example.zzan.user.dto.UserRequestDto;
import com.example.zzan.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import com.example.zzan.global.util.JwtUtil;
import com.example.zzan.user.service.KakaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    ///login/oauth2/code/kakao
    @GetMapping("/login/oauth2/code/kakao")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        String createToken = kakaoService.kakaoLogin(code, response);

        // Cookie 생성 및 직접 브라우저에 Set
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, createToken.substring(7));
        cookie.setPath("/");
        response.addCookie(cookie);

        return "index";
    }
    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody UserRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            for(FieldError fieldError: bindingResult.getFieldErrors()) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return userService.signup(requestDto);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserLoginDto requestDto, HttpServletResponse response) {
        return userService.login(requestDto, response);
    }

    @GetMapping("/logout/{userEmail}")
    public ResponseEntity logout(@PathVariable String userEmail) {
       return userService.logout(userEmail);
    }

    @PutMapping("/{userId}/like")
    public ResponseDto<String> likeUser(@PathVariable Long userId) {
        userService.updateAlcohol(userId, true);
        return ResponseDto.setSuccess("도수를 올렸습니다.");
    }

    @PutMapping("/{userId}/dislike")
    public ResponseDto<String> dislikeUser(@PathVariable Long userId) {
        userService.updateAlcohol(userId, false);
        return ResponseDto.setSuccess("도수를 내렸습니다.");
    }
}

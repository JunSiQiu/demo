package com.example.springboot.controller.dto;

import lombok.Data;

/**
 * 接收前端的登录请求
 */
@Data
public class UserDto {
    private String username;
    private String avatarUrl;
    private String password;
    private String nickname;
    private String token;
}

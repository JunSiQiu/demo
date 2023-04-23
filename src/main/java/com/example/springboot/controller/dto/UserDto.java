package com.example.springboot.controller.dto;

import com.example.springboot.entity.Menu;
import lombok.Data;

import java.util.List;

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
    private String role;
    private List<Menu> menus;
}

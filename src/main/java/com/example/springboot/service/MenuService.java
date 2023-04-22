package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Menu;
import com.example.springboot.mapper.MenuMapper;
import org.springframework.stereotype.Service;

@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu> {
}

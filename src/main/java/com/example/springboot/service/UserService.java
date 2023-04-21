package com.example.springboot.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.UserDto;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.utils.TokenUtils;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    public Boolean saveUser(User user) {
//        System.out.println(user.getNickname());
//        if(user.getId() == null){
//            return save(user);  // mybatis-plus提供的方法表示插入数据
//        }else{
//            return updateById(user);
//        }
        return saveOrUpdate(user);
    }

    public UserDto login(UserDto userDto) {
        User one = getUserInfo(userDto);
        if(one != null){
            BeanUtil.copyProperties(one, userDto, true);
            // 设置token
            String token= TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDto.setToken(token);
            return userDto;
        }else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    public User register(UserDto userDto) {
        User one = getUserInfo(userDto);
        if(one == null){
            one = new User();
            BeanUtil.copyProperties(userDto, one, true);
            save(one);      //把copy之后的对象存储到数据库
        }else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }

    private User getUserInfo(UserDto userDto){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userDto.getUsername());
        queryWrapper.eq("password",userDto.getPassword());
        User one;
        try {
            one = getOne(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

//    @Autowired
//    private UserMapper userMapper;

//    public int save(User user){
//        if(user.getId() == null){ //user没有ID则表示新增
//            return userMapper.insert(user);
//        }else {  //否则为更新
//            return userMapper.update(user);
//        }
//    }
}

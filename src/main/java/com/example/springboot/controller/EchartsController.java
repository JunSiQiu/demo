package com.example.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import com.example.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static final String USER_KEY = "USER_FRONT_ALL";

    @GetMapping("/example")
    public Result get(){
        Map<String, Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y", CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);
    }

    @GetMapping("/members")
//    @Cacheable(value = "user" ,key = "targetClass + methodName")
    public Result members(){
        // 1.从缓存取数据
        String jsonStr = stringRedisTemplate.opsForValue().get(USER_KEY);
        List<User> list = new ArrayList<>();
        if(StrUtil.isBlank(jsonStr)){    // 2.取出的json为空
            list = userService.list();   // 3.从数据库中取数据
            // 4.再缓存到redis
            stringRedisTemplate.opsForValue().set(USER_KEY,JSONUtil.toJsonStr(list));
        }else{
            // 5.如果有，从redis缓存中获取数据
            list = JSONUtil.toBean(jsonStr, new TypeReference<List<User>>() {
            }, true);
        }
        int q1=0;   // 第一季度
        int q2=0;   // 第二季度
        int q3=0;   // 第三季度
        int q4=0;   // 第四季度
        for(User user : list){
            Date createTime = user.getCreateTime();
            Quarter quarter = DateUtil.quarterEnum(createTime);
            switch (quarter) {
                case Q1: q1 +=1; break;
                case Q2: q2 +=1; break;
                case Q3: q3 +=1; break;
                case Q4: q4 +=1; break;
                default: break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));
    }
}

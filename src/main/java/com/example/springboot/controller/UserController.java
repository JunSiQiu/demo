package com.example.springboot.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.UserDto;
import com.example.springboot.entity.User;
import com.example.springboot.service.UserService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    // 登录
    @PostMapping("/login")
    public Result login(@RequestBody UserDto userDto){
        UserDto dto = userService.login((userDto));
        return Result.success(dto);
    }

    // 注册
    @PostMapping("/register")
    public Result register(@RequestBody UserDto userDto){
        return Result.success(userService.register(userDto));
    }

    // 新增和修改
    @PostMapping("/save")
    public Result save(@RequestBody User user){
        // 新增或者更新
        return Result.success(userService.saveUser(user));
    }

    // 查询所有数据
    @GetMapping
    public Result findALL(){
        return Result.success(userService.list());
    }

    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username){
        QueryWrapper<User> queryWrapper =  new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(userService.getOne(queryWrapper));
    }

    // 删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success(userService.removeById(id));
    }

    // 批量删除
    @PostMapping("/del/batch")
    public Result deletebatch(@RequestBody List<Integer> ids){
        return Result.success(userService.removeByIds(ids));
    }



    // 分页查询
    // 接口路径: /user/page?pageNum=1&pageSize=10
    // limit第一个参数 = (pageNum-1)*pageSize 第二个参数为pageSize
//    @GetMapping("/page")
//    public Map<String, Object> findPage(@RequestParam Integer pageNum,
//                                        @RequestParam Integer pageSize,
//                                        @RequestParam String username){
//        pageNum = (pageNum-1)*pageSize;
//        List<User> data = userMapper.selectPage(pageNum,pageSize,username);
//        Integer total = userMapper.selectTotal(username);
//        Map<String, Object> res= new HashMap<>();
//        res.put("data",data);
//        res.put("total",total);
//        return res;
//    }

    // 分页查询 mybatis-plus实现
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String username,
                                @RequestParam(defaultValue = "") String email,
                                @RequestParam(defaultValue = "") String address){
        IPage<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(!"".equals(username))
            queryWrapper.like("username",username);
        if(!"".equals(email))
            queryWrapper.like("email",email);
        if(!"".equals(address))
            queryWrapper.like("address",address);

        // 获取当前用户信息
        User currentUser =  TokenUtils.getCurrentUser();

        return Result.success(userService.page(page, queryWrapper));
    }

    // 导出
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{
        List<User> list = userService.list();
        // 内存操作，写到浏览器
        ExcelWriter write = ExcelUtil.getWriter(true);
        // 自定义标题别名
        write.addHeaderAlias("username","用户名");
        write.addHeaderAlias("password","密码");
        write.addHeaderAlias("nickname","昵称");
        write.addHeaderAlias("email","邮箱");
        write.addHeaderAlias("phone","电话");
        write.addHeaderAlias("address","地址");
        write.addHeaderAlias("createTime","创建时间");
        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        write.write(list,true);
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+fileName+".xlsx");

        ServletOutputStream out = response.getOutputStream();
        write.flush(out,true);
        out.close();
        write.close();
    }

    // 导入
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        reader.addHeaderAlias("用户名","username");
        reader.addHeaderAlias("密码","password");
        reader.addHeaderAlias("昵称","nickname");
        reader.addHeaderAlias("邮箱","email");
        reader.addHeaderAlias("电话","phone");
        reader.addHeaderAlias("地址","address");
        reader.addHeaderAlias("创建时间","createTime");
        List<User> list = reader.readAll(User.class);
        System.out.println(list);
        // 插入数据库
        userService.saveBatch(list);
        return Result.success(true);
    }

}

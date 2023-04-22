package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Role;
import com.example.springboot.service.RoleService;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    // 新增或者修改
    @PostMapping("/save")
    public Result save(@RequestBody Role role){
        // 新增或者更新\
        return Result.success(roleService.saveOrUpdate(role));
    }

    // 查询所有数据
    @GetMapping
    public Result findALL(){
        return Result.success(roleService.list());
    }

    // 删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success(roleService.removeById(id));
    }

    // 批量删除
    @PostMapping("/del/batch")
    public Result deletebatch(@RequestBody List<Integer> ids){
        return Result.success(roleService.removeByIds(ids));
    }

    // 分页查询 mybatis-plus实现
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name){
        IPage<Role> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if(!"".equals(name))
            queryWrapper.like("name",name);
        queryWrapper.orderByAsc("id");
        return Result.success(roleService.page(page, queryWrapper));
    }

    /**
     * 绑定角色与菜单的关系
     * @param roleId 角色id
     * @param menuIds 菜单id数组
     * @return
     */
    @PostMapping("/roleMenu/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds){
        roleService.setRoleMenu(roleId, menuIds);
        return Result.success();
    }

    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId){
        return Result.success(roleService.getRoleMenu(roleId));
    }

}

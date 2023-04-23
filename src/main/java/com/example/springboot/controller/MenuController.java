package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Dict;
import com.example.springboot.entity.Menu;
import com.example.springboot.mapper.DitcMapper;
import com.example.springboot.service.MenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Resource
    private MenuService menuService;

    @Resource
    private DitcMapper ditcMapper;

    // 新增或者修改
    @PostMapping("/save")
    public Result save(@RequestBody Menu menu){
        // 新增或者更新\
        return Result.success(menuService.saveOrUpdate(menu));
    }

    // 查询所有数据
    @GetMapping
    public Result findALL(@RequestParam(defaultValue = "") String name){
        return Result.success(menuService.findMenus(name));
    }

    // 删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success(menuService.removeById(id));
    }

    // 批量删除
    @PostMapping("/del/batch")
    public Result deletebatch(@RequestBody List<Integer> ids){
        return Result.success(menuService.removeByIds(ids));
    }

    // 分页查询 mybatis-plus实现
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String name){
        IPage<Menu> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name);
        queryWrapper.orderByAsc("id");
        return Result.success(menuService.page(page, queryWrapper));
    }

    @GetMapping("/icons")
    public Result getIcons(){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
        return Result.success(ditcMapper.selectList(null));
    }
}

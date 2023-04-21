package com.example.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Files;
import com.example.springboot.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件上传相关接口
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private FileMapper fileMapper;

    /**
     * 新增和更新接口
     * @param files
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody Files files){
        return Result.success(fileMapper.updateById(files));
    }

    /**
     * 文件上传接口
     * @param file 前端传递过来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename =  file.getOriginalFilename();
        //String type = file.getContentType();
        String type =  FileUtil.extName(originalFilename);
        long size = file.getSize();

        //TODO 先存磁盘
        File uploadParentFile = new File(fileUploadPath);
        // 判断配置的文件目录是否存在,若不存在则创建一个新的文件目录
        if(!uploadParentFile.exists()){
            uploadParentFile.mkdir();
        }
        // 定义一个文件唯一标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUuid = uuid + StrUtil.DOT + type;   // StrUtil.DOT为.
        File uploadFile = new File(fileUploadPath + fileUuid);

        // 上传文件到磁盘
        file.transferTo(uploadFile);
        // 获取文件的md5
        String md5 = SecureUtil.md5(uploadFile);
        // 从数据库查是否存在相同的路径
        Files dbFiles = getFileByMd5(md5);

        System.out.println(md5);
        // 获取文件的url
        String url;
        if(dbFiles != null){       // 文件存在
            url = dbFiles.getUrl();
            // 由于文件已存在，所有删除上传的重复文件
            uploadFile.delete();
        }else{                   // 数据库不存在重复文件，不删除上传的文件
            url = "http://localhost:8081/file/" + fileUuid;
            //把获取到的文件存储到磁盘目录
        }

        //TODO 存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);

        return url;
    }

    /**
     * 文件下载接口  http://localhost:8081/file/{ fileUuid}"
     * @param fileUuid
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUuid}")
    public void download(@PathVariable String fileUuid, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取图片
        File uploadFile = new File(fileUploadPath + fileUuid);
        //设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUuid, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取上传字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    /**
     * 通过文件的md5查询文件
     * @param md5
     * @return
     */
    private Files getFileByMd5(String md5){
        // 查询文件md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5",md5);
        List<Files> filesList =  fileMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null :filesList.get(0);
    }

    /**
     * 分页查询接口
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name){
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        // 查询未删除的记录
        queryWrapper.eq("is_delete",false);
        //orderByAsc为正序, orderByDesc为倒序
        queryWrapper.orderByAsc( "id");
        if(!"".equals(name)){
            queryWrapper.like("name",name);
        }
        IPage<Files> page = new Page<>(pageNum,pageSize);
        return Result.success(fileMapper.selectPage(page, queryWrapper));
    }

    // 删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        Files files = fileMapper.selectById(id);
        files.setIs_delete(true);
        return Result.success(fileMapper.updateById(files));
    }

    // 批量删除
    @PostMapping("/del/batch")
    public Result deletebatch(@RequestBody List<Integer> ids){
        // select * from sys_file where id in (id,id,id ...)
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",ids);
        List<Files> filesList = fileMapper.selectList(queryWrapper);
        for(Files file : filesList){
            file.setIs_delete(true);
            fileMapper.updateById(file);
        }
        return Result.success();
    }
}

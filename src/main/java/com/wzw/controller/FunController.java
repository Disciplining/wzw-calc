package com.wzw.controller;

import com.wzw.common.CommonResult;
import com.wzw.entity.Circle;
import com.wzw.service.FunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class FunController
{
    @Autowired
    private FunService funService;

    /**
     * 上传文件并计算
     * @param file 文件
     * @return 计算出的数据
     */
    @PostMapping("/upload-calc")
    private CommonResult<List<Circle>> uploadFileAndCalc(MultipartFile file)
    {
        return funService.uploadFileAndCalc(file);
    }
}

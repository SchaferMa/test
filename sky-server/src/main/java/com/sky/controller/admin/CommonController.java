/**
 * FileName: CommonController
 * Author:   mayuchao
 * Date:     2024/1/11 22:14
 * Description: 共用接口
 */
package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * 〈功能简述〉<br>
 * 〈共用接口〉
 * @author mayuchao
 * @create 2024/1/11
 */
@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api(tags = "公共接口")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @ApiOperation("文件上传功能")
    public Result<String> filesUpload(@RequestParam("file") MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        String upload = aliOssUtil.upload(file.getBytes(), uuid.toString() + now.toString() + ".jpg");
        return Result.success(upload);

    }
}

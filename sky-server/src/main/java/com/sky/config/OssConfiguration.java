/**
 * FileName: OssConfiguration
 * Author:   mayuchao
 * Date:     2024/1/11 22:36
 * Description: 阿里云配置类
 */
package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 〈功能简述〉<br>
 * 〈实例化aliOssUtil,@bean注解保证项目启动时自动创建
 * @author mayuchao
 * @create 2024/1/11
 */
@Configuration
@Slf4j
public class OssConfiguration {
    @Bean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
}

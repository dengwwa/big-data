package com.dengwwa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:Hello
 * Author:dengww
 * Date:2025/4/30
 */
@Api(value = "test",tags = "tag-测试")
@RestController
public class TestController {
    @ApiOperation(value = "test")
    @GetMapping("/test")
    public String hello(){
        return "Hello Test";
    }
}

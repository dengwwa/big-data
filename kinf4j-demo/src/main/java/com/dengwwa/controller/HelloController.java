package com.dengwwa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:Hello
 * Author:dengww
 * Date:2025/4/30
 */
@Api(value = "hello",tags = "tag-Hello")
@RestController
public class HelloController {
    @ApiOperation(value = "hello")
    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }
}

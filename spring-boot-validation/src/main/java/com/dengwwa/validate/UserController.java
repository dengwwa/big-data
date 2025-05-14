package com.dengwwa.validate;

import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * Description: @Valid 验证
 * Author:dengww
 * Date:2025/5/14
 */
@RequestMapping("/api/user")
@RestController
public class UserController {
    @PostMapping("/add")
    public String createUser(@Valid @RequestBody User user) {
        // 业务逻辑
        return "用户创建成功";
    }

    @Data
    static class User {
        @NotBlank(message = "用户名不能为空")
        private String username;

    }
}




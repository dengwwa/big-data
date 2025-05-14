package com.dengwwa.validated.group;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Description:
 * Author:dengww
 * Date:2025/5/14
 */

@RequestMapping("/api/validated/user")
@RestController
public class UserController2 {
    @PostMapping("/add")
    public String createUser(@Validated(Create.class) @RequestBody User user) {
        // 创建用户逻辑
        return "用户创建成功";
    }

    @PutMapping("/update/{id}")
    public String updateUser(@PathVariable Long id,
                             @Validated(Update.class) @RequestBody User user) {
        // 更新用户逻辑
        return "用户更新成功";
    }

    @PutMapping("/users/admin/{id}")
    public String adminUpdateUser(@PathVariable Long id,
                                  @Validated(SuperAdmin.class) @RequestBody User user) {
        // SuperAdmin组会触发Create和Update组的所有验证
        return "管理员更新用户成功";
    }
}

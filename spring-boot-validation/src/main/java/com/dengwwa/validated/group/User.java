package com.dengwwa.validated.group;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Description:
 * Author:dengww
 * Date:2025/5/14
 */
@Data
public class User {
    private Long id;

    @NotBlank(groups = {Create.class, Update.class}, message = "用户名不能为空")
    private String username;

    @NotBlank(groups = Create.class, message = "创建时密码不能为空")
    @Size(min = 6, max = 20, groups = Create.class, message = "密码长度必须在6-20之间")
    private String password;

    // 无参构造函数
    public User() {}
}

package com.dengwwa.validated;

import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RequestMapping("/api/product")
@RestController
@Validated // 类级别的验证
public class ProductController {

    @PostMapping("/add")
    public String createProduct(@Validated @RequestBody Product product) {
        // 业务逻辑
        return "产品创建成功";
    }

    @Data
    static class Product {
        @NotBlank(message = "产品名称不能为空")
        private String name;

        // 无参构造函数
        public Product() {}

    }
}


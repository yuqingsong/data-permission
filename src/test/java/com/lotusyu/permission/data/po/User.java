package com.lotusyu.permission.data.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lotusyu.permission.data.annotation.DataPermission;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Auther: yqs
 * @Date: 2023/6/1
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@DataPermission
public class User {
    Long id;
    String name;
    int age;
}

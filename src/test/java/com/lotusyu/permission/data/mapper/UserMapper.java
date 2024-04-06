package com.lotusyu.permission.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lotusyu.permission.data.po.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Auther: yqs
 * @Date: 2023/6/1
 * @Description:
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User getUserById(Long userId);
}

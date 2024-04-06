package com.lotusyu.permission.data.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lotusyu.permission.data.annotation.DataPermission;
import com.lotusyu.permission.data.annotation.DataPermissions;
import com.lotusyu.permission.data.mapper.UserMapper;
import com.lotusyu.permission.data.po.User;
import com.lotusyu.permission.data.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: yqs
 * @Date: 2023/6/1
 * @Description:
 */
@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {

    public IUserServiceImpl() {
        System.out.println(this.getClass());
    }


    @Override
    public List<User> getAll() {
        return super.list();
    }

    @Override
    public List<User> getAllWithWhere() {
        return super.list();
    }

    @Override
    public List<User> getAllWithFunction() {
        return super.list();
    }


}

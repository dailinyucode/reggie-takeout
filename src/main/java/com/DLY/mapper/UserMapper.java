package com.DLY.mapper;

import com.DLY.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86178
* @description 针对表【user(用户信息)】的数据库操作Mapper
* @createDate 2022-04-25 21:10:16
* @Entity com.DLY.pojo.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}





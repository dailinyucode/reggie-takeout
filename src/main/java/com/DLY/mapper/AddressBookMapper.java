package com.DLY.mapper;

import com.DLY.pojo.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86178
* @description 针对表【address_book(地址管理)】的数据库操作Mapper
* @createDate 2022-04-26 13:58:53
* @Entity com.DLY.pojo.AddressBook
*/
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}





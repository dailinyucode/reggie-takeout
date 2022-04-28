package com.DLY.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.DLY.pojo.AddressBook;
import com.DLY.service.AddressBookService;
import com.DLY.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author 86178
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2022-04-26 13:58:53
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}





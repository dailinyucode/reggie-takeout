package com.DLY.controller;

import com.DLY.common.BaseContext;
import com.DLY.common.R;
import com.DLY.pojo.AddressBook;
import com.DLY.service.AddressBookService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    AddressBookService addressBookService;

    /**
     * 保存并返回
     * @param addressBook
     * @return
     */
    @PostMapping
    public R save(
            @RequestBody AddressBook addressBook
    ){
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 将某一个地址设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("default")
    public R setDefault(
            @RequestBody AddressBook addressBook
    ){
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault,0);
        addressBookService.update(wrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);

        return R.success(addressBook);
    }
    @GetMapping("/{id}")
    public R get(
            @PathVariable Long id
    ){
        AddressBook addressBook = addressBookService.getById(id);
        if(addressBook!=null){
            return R.success(addressBook);
        }else{
            return R.error("没有数据");
        }
    }
    @GetMapping("default")
    public R getDefault(){
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        wrapper.eq(AddressBook::getIsDefault,1);
        AddressBook one = addressBookService.getOne(wrapper);
        if(null==one){
            return R.error("没有该对象");
        }else{
            return R.success(one);
        }
    }

    /**
     * 根据userid来地址
     * @param addressBook
     * @return
     */
    @GetMapping("/list")
    public R list(
            AddressBook addressBook
    ){
        addressBook.setUserId(BaseContext.getCurrentId());
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(null!=addressBook.getUserId(),AddressBook::getUserId,addressBook.getUserId());
        wrapper.orderByDesc(AddressBook::getUpdateTime);

        return R.success(addressBookService.list(wrapper));
    }
}

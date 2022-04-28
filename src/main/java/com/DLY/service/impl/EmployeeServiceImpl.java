package com.DLY.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.DLY.pojo.Employee;
import com.DLY.service.EmployeeService;
import com.DLY.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author 86178
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2022-04-23 09:10:49
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
implements EmployeeService{

}

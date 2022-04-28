package com.DLY.mapper;

import com.DLY.pojo.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86178
* @description 针对表【employee(员工信息)】的数据库操作Mapper
* @createDate 2022-04-23 09:10:49
* @Entity com.DLY.pojo.Employee
*/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}

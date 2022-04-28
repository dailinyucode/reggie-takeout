package com.DLY.controller;

import com.DLY.common.BaseContext;
import com.DLY.common.R;
import com.DLY.pojo.Employee;
import com.DLY.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import sun.security.provider.MD5;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R login(HttpSession session,
                   @RequestBody Employee employee){
        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(employeeLambdaQueryWrapper);
        if(emp==null){
            return R.error("没有该用户");
        }
        if(!password.equals(emp.getPassword())){
            return R.error("密码不正确");
        }
        if(emp.getStatus()==0){
            return R.error("账号已禁用");
        }
        //登入成功，将员工号存入session并返回登入结果
        session.setAttribute("employee",emp.getId());

        return R.success(emp);
    }

    @PostMapping("/logout")
    public R logout(HttpSession session){
        session.removeAttribute("employee");
        return R.success("退出成功！");
    }

    /**
     * 新增员工信息
     * @param employee
     * @return
     */
    @PostMapping
    public R save(HttpSession session,@RequestBody Employee employee){
        //设置初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        通过filter过滤器进行设置了
//        Long empId = (Long) session.getAttribute("employee");
//        //设置创建用户和跟新用户
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
//        BaseContext.setCurrentId(empId);

        employeeService.save(employee);

        return R.success(null);
    }

    /**
     * 修改信息
     * @param employee
     * @return
     */
    @PutMapping
    public R update(HttpSession session,
                    @RequestBody Employee employee){
        //设置修改人的信息id
//        Long empId = (Long) session.getAttribute("employee");
//        BaseContext.setCurrentId(empId);
//        employee.setUpdateUser(empId);

        employeeService.updateById(employee);
        return R.success(null);
    }

    /**
     * 分页和信息查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R page(
            //当前页
            @RequestParam Integer page,
            //页面大小
            @RequestParam Integer pageSize,
            //查询参数  可以不存在
            @RequestParam(required = false) String name
    ){
        //第一个参数是位置
        Page<Employee> employeePage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(name!=null){
            employeeLambdaQueryWrapper.like(Employee::getName,name);
        }
        return R.success(employeeService.page(employeePage,employeeLambdaQueryWrapper));
    }

    /**
     * 根据id获取用户信息   js无法接收雪花算法的id大小，应该用字符串进行接收
     * @return
     */
    @GetMapping("/{id}")
    public R getById(
            @PathVariable("id") String id
    ){
        return R.success(employeeService.getById(id));
    }

}

package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        String passwords = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!passwords.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus().equals(StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     */
    @Override
    public void newlyIncreased(EmployeeDTO employeeDTO) {
        //使用工具类拷贝相同字段
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        //补全字段
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setStatus(StatusConstant.ENABLE);
        employeeMapper.insert(employee);
    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pagingQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //分页条件
        Page<Employee> employeePage = new Page<>(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        //判断name是否为null
        if (employeePageQueryDTO.getName() != null) {
            //查询条件
            LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
            LambdaQueryWrapper<Employee> like = wrapper.like(Employee::getName, employeePageQueryDTO.getName());
            //根据name模糊查询
            Page<Employee> employeePage1 = employeeMapper.selectPage(employeePage, like);
            return new PageResult(employeePage1.getTotal(), employeePage1.getRecords());
        }
        //查询所有并分页
        Page<Employee> page = employeeMapper.selectPage(employeePage, new LambdaQueryWrapper<>());
        return new PageResult(page.getTotal(), page.getRecords());
    }

    /**
     * 启用禁用
     *
     * @param status
     * @param id
     */
    @Override
    public void state(Integer status, Long id) {
        Employee employee = Employee.builder().status(status).id(id).build();
        employeeMapper.updateById(employee);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public Employee query(long id) {
        Employee employee = employeeMapper.selectById(id);
        return employee;
    }

    /**
     * 编辑员工信息
     *
     * @param employeeDTO
     */
    @Override
    public void compile(EmployeeDTO employeeDTO) {
        //补全
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employeeMapper.updateById(employee);

    }

}

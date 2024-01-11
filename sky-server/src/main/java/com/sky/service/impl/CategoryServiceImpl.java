/**
 * FileName: CategoryServiceImpl
 * Author:   mayuchao
 * Date:     2024/1/10 20:51
 * Description: 分类模块
 */
package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.MessageConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 〈功能简述〉<br>
 * 〈分类模块〉
 * @author mayuchao
 * @create 2024/1/10
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setStatus(1);
        BeanUtils.copyProperties(categoryDTO,category);
        categoryMapper.insert(category);

    }

    /**
     * 启用禁用
     * @param status
     * @param id
     */
    @Override
    public void startUsing(Integer status, long id) {
        //根据id修改status
        Category category = Category.builder().id(id).status(status).build();
        categoryMapper.updateById(category);
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageSelect(CategoryPageQueryDTO categoryPageQueryDTO) {
        //设置分页条件
        Page<Category> page = new Page<>(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        //设置查询条件
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        //如果name和type都不为null,那么根据name和type查询
        if (categoryPageQueryDTO.getName() != null && categoryPageQueryDTO.getType() != null) {
            wrapper.eq(Category::getType, categoryPageQueryDTO.getType())
                    .like(Category::getName, categoryPageQueryDTO.getName())
                    .orderByAsc(Category::getSort);
            //查询
            categoryMapper.selectPage(page, wrapper);
            //封装返回
            return new PageResult(page.getTotal(),page.getRecords());
        }
        //如果name和type都为null,查询所有
        else if (categoryPageQueryDTO.getName() == null && categoryPageQueryDTO.getType() == null) {
            wrapper.orderByAsc(Category::getSort);
            categoryMapper.selectPage(page,wrapper);
            return new PageResult(page.getTotal(), page.getRecords());
        }
        //如果name不为null,type为null,根据name查询
        else if (categoryPageQueryDTO.getName() != null) {
            wrapper.like(Category::getName, categoryPageQueryDTO.getName());
            categoryMapper.selectPage(page,wrapper);
            return new PageResult(page.getTotal(), page.getRecords());
        }
        //如果name为null,type不为null,根据type查询
        wrapper.eq(Category::getType, categoryPageQueryDTO.getType())
                .orderByAsc(Category::getSort);
        categoryMapper.selectPage(page,wrapper);
        return new PageResult(page.getTotal(), page.getRecords());
    }

    /**
     * 修改分类
     * @param categoryDTO
     */
    @Override
    public void amend(CategoryDTO categoryDTO) {
        //赋值
        Category build = Category.builder().build();
        BeanUtils.copyProperties(categoryDTO,build);
        categoryMapper.updateById(build);
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @Override
    public Result delete(long id) {
        //统计当前分类下菜品表信息
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, id);
        Integer count = dishMapper.selectCount(wrapper);
        //统计当前分类下套餐表信息
        LambdaQueryWrapper<Setmeal> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Setmeal::getCategoryId, id);
        Integer count1 = setmealMapper.selectCount(wrapper1);
        //判断统计的数量是否大于0
        if (count > 0) {
            return Result.error(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        } else if (count1 > 0) {
            return Result.error(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        categoryMapper.deleteById(id);
        return Result.success();
    }


}

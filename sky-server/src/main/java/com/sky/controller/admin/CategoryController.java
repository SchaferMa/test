/**
 * FileName: CategoryController
 * Author:   mayuchao
 * Date:     2024/1/10 20:43
 * Description: 分类模块
 */
package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 〈功能简述〉<br>
 * 〈分类模块〉
 * @author mayuchao
 * @create 2024/1/10
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类模块相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @ApiOperation("新增分类")
    @PostMapping
    public Result save(@RequestBody CategoryDTO categoryDTO) {
        //调用service
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 启用禁用
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("启用禁用")
    @PostMapping("/status/{status}")
    public Result startUsing(@PathVariable Integer status, long id){
        categoryService.startUsing(status,id);
        return Result.success();
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @ApiOperation("分类分页查询")
    @GetMapping("/page")
    public Result<PageResult> pageSelect(CategoryPageQueryDTO categoryPageQueryDTO){
        PageResult pageResult = categoryService.pageSelect(categoryPageQueryDTO);
        return Result.success(pageResult);

    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @ApiOperation("根据类型查询分类")
    @GetMapping("/list")
    public Result selectList(Integer type){
        return categoryService.selectList(type);
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @ApiOperation("修改分类")
    @PutMapping
    public Result amend(@RequestBody CategoryDTO categoryDTO){
        categoryService.amend(categoryDTO);
        return Result.success();
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @ApiOperation("删除分类")
    @DeleteMapping
    public Result delete(long id){
        return categoryService.delete(id);

    }

}

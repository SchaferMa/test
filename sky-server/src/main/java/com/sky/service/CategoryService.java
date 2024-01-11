package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;

public interface CategoryService {
    void save(CategoryDTO categoryDTO);

    void startUsing(Integer status, long id);

    PageResult pageSelect(CategoryPageQueryDTO categoryPageQueryDTO);

    void amend(CategoryDTO categoryDTO);

    Result delete(long id);
}

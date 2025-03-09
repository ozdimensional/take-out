package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;


public interface DishService {

    /*
     * 新增菜品
     * @param dishDTO
     */
    void saveWithFlavors(DishDTO dishDTO);

    /*
     * 查看菜品
     * @param dishPageQueryDTO
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /*
     * 删除菜品
     *  @param ids
     */
    void deleteBench(List<Long> ids);

    /*
     * 根据id获取菜品及其所有配料信息
     * @param id
     */
    DishVO getByIdWithFlavors(Long id);

    /*
     * 修改菜品及其配料信息
     * @param dishDTO
     */
    void updateWithFlavors(DishDTO dishDTO);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);

    /**
     * 根据菜品信息查询菜品及其配料信息
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);

    /**
     * 起售或下架菜品
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);
}

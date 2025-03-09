package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /*
     * 批量插入口味数据
     */
    void insertBatch(List<DishFlavor> flavors);

    void deleteByDishId(Long dishId);

    /*
     * 根据菜品id查询口味数据
     */
    @Select("SELECT * FROM dish_flavor WHERE dish_id = #{id}")
    List<DishFlavor> getByDishId(Long id);
}

package com.sky.service;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShoppingCartService {

    /*
     * 添加购物车
     * @param shoppingCartDTO
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /*
     * 展示购物车
     * @param id
     */
    List<ShoppingCart> list();

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     *  清空购物车
     */
    void clean();
}

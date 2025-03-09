package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "商铺管理")
@Slf4j
public class ShopController {

    public static final String SHOP_STATUS = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;
    /*
     *设置营业状态
     */
    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    public Result setShopStatus(@PathVariable Integer status)
    {
        log.info("设置营业状态：{}", status);
        redisTemplate.opsForValue().set(SHOP_STATUS, status);
        return Result.success();
    }

    /*
     *获取营业状态
     */
    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result<Integer> getShopStatus() {
        Integer status = (Integer)redisTemplate.opsForValue().get(SHOP_STATUS);
        log.info("获取营业状态");
        return Result.success(status);
    }

}

package com.leyou.item.mapper;

import com.leyou.item.pojo.Sku;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * create by: cdy
 * description:$
 * create time: $ $
 * <p>
 * $
 *
 * @return $
 */
public interface SkuMapper extends Mapper<Sku> {
    @Select("SELECT * FROM TB_SKU WHERE SPU_ID=#{spuId}")
    List<Sku> querySkusBySpuId(Long spuId);
}

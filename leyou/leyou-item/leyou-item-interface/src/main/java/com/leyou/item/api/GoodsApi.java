package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
@RequestMapping
public interface GoodsApi {

    @GetMapping("spu/page")
     PageResult<SpuBo> querySpu(
            @RequestParam(name = "key", required = false) String key,
            @RequestParam(name = "saleable", required = false) Boolean saleable,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "rows", defaultValue = "5") Integer rows);
    @GetMapping("spu/detail/{spuId}")
    SpuDetail querySpuDetail(@PathVariable(name="spuId",required = true)Long spuId);
    @GetMapping("sku/list")
    List<Sku> querySkuBySpuId(@RequestParam(name = "id")Long id);
    @GetMapping("spu/{id}")
    Spu querySpuById(@PathVariable(name = "id") Long id);
}

package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
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
@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpu(
            @RequestParam(name = "key", required = false) String key,
            @RequestParam(name = "saleable", required = false) Boolean saleable,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "rows", defaultValue = "5") Integer rows) {
        PageResult<SpuBo> spuPageResult = this.goodsService.querySpu(key, saleable, page, rows);
        if (CollectionUtils.isEmpty(spuPageResult.getItems())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuPageResult);

    }

    @PostMapping
    public ResponseEntity<Void> saveSpuAndSkuInfo(@RequestBody SpuBo spuBo) {

        this.goodsService.saveSpuAndSkuInfo(spuBo);
        return ResponseEntity.ok().build();
    }
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetail(@PathVariable(name="spuId",required = true)Long spuId){

        SpuDetail spuDetail = this.goodsService.querySpuDetail(spuId);
        if(spuDetail==null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(spuDetail);
    }
    @GetMapping("sku/list")
    public  ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam(name = "id")Long id){
        List<Sku> skuList = this.goodsService.querySkuBySpuId(id);
        if(CollectionUtils.isEmpty(skuList))
            return ResponseEntity.notFound().build();
        return  ResponseEntity.ok(skuList);
    }
    @PutMapping("goods")
    public  ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){
          this.goodsService.updateGoods(spuBo);
        return  ResponseEntity.ok().build();
    }
    @DeleteMapping("spu/delete/{id}")
    public  ResponseEntity<Void> deleteSpu(@PathVariable(name="id")Long id){
        this.goodsService.deleteSpu(id);
        return  ResponseEntity.ok().build();
    }

    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable(name = "id") Long id){
        Spu spu=this.goodsService.querySpuByID(id);
        if(spu==null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spu);
    }
    @GetMapping("sku/{id}")
    public ResponseEntity<Sku> querySkuById(@PathVariable("id")Long id){
        Sku sku = this.goodsService.querySkuById(id);
        if(null == sku){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sku);
    }

}

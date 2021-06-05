package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
@Controller
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByKey(
            @RequestParam(name = "key", required = false) String key,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "rows", defaultValue = "5") Integer rows,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "desc", required = false) Boolean desc
    ) {
//        if(key=="")
//            return  ResponseEntity.badRequest().build();
        PageResult<Brand> pageResult = this.brandService.queryBrandByKey(key, page, rows, sortBy, desc);
        if (CollectionUtils.isEmpty(pageResult.getItems())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pageResult);
    }

    /*
     * @descript:
     * @param
     * @return org.springframework.http.ResponseEntity<java.lang.Void>
     * @author cdy
     * @creed:
     * @date 2021-05-18 22:49
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam(name = "cids", required = true) List<Long> cids) {

        this.brandService.saveBrand(brand, cids);

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam(name = "cids", required = true) List<Long> cids) {

        this.brandService.updateBrand(brand, cids);

        return ResponseEntity.ok().build();
    }

    @PutMapping("delete")
    public ResponseEntity<Void> deleteBrand(Brand brand) {
        this.brandService.deleteBrand(brand);
        return ResponseEntity.ok().build();
    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable(name = "cid") Long cid) {
        List<Brand> brands = this.brandService.queryBrandByCid(cid);
        if (CollectionUtils.isEmpty(brands)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brands);
    }
    @GetMapping("bname/{id}")
    public  ResponseEntity<Brand> queryBrandName(@RequestParam(name = "id")Long bid){
        Brand brand= this.brandService.queryBrandName(bid);
        if(brand==null){
            return ResponseEntity.notFound().build();
        }
          return  ResponseEntity.ok(brand);
    }
}

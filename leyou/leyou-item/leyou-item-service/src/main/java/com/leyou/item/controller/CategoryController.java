package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
@Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoriesByPid(@RequestParam(value = "pid", defaultValue = "0") Long pid) {

        if (pid == null || pid < 0)
            return ResponseEntity.badRequest().build();
        List<Category> categories = this.categoryService.queryCategoriesByPid(pid);
        if (categories == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryCatagoriesByBid(@PathVariable(value = "bid") Long bid) {
        if (bid == null || bid < 0)
            return ResponseEntity.badRequest().build();
        List<Category> categories = this.categoryService.queryCategoriesByBid(bid);
        if (categories == null || categories.size() == 0)
            return  ResponseEntity.notFound().build();

        return ResponseEntity.ok(categories);
    }
    @GetMapping("names")
    public  ResponseEntity<List<String>> queryCategoriesName(@RequestParam(name = "ids")List<Long> ids){
        List<String> list = this.categoryService.queryNamesById(ids);
        if(CollectionUtils.isEmpty(list)){
            return  ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(list);
    }
}

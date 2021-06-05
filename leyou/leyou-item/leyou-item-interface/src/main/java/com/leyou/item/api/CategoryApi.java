package com.leyou.item.api;

import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("category")
public interface CategoryApi {


    @GetMapping("names")
    List<String> queryCategoriesName(@RequestParam(name = "ids")List<Long> ids);
}

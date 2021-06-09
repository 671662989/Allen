package com.leyou.search.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping
public class SearchController {

    @Autowired
    private SearchService service;

    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> searchPage(@RequestBody SearchRequest request){

       PageResult<Goods> result =this.service.searchPage(request);
       if(request.getSize()==0){
           return  ResponseEntity.notFound().build();
       }
        return ResponseEntity.ok(result);
    }
}

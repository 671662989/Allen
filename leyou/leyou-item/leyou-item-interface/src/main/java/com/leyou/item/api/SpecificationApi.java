package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
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
@RequestMapping("spec")
public interface SpecificationApi {

    @GetMapping("params")
    List<SpecParam> queryParams(
            @RequestParam(name="gid",required = false)Long gid,
            @RequestParam(name="cid",required = false)Long cid,
            @RequestParam(name="generic",required = false)Boolean generic,
            @RequestParam(name="searching",required =  false)Boolean searching);

    @GetMapping("{cid}")
    List<SpecGroup> querySpecByCid(@PathVariable(name = "cid") Long cid);

}

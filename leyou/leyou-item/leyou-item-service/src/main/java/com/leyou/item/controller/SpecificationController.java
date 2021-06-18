package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService service;
     @GetMapping("groups/{pid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsByPID(@PathVariable("pid")Long pid){

         List<SpecGroup> specGroups = this.service.queryGroupsByPID(pid);
         if(CollectionUtils.isEmpty(specGroups))
             return ResponseEntity.notFound().build();
         return ResponseEntity.ok(specGroups);
     }
     @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParams(
            @RequestParam(name="gid",required = false)Long gid,
            @RequestParam(name="cid",required = false)Long cid,
            @RequestParam(name="generic",required = false)Boolean generic,
            @RequestParam(name="searching",required =  false)Boolean searching){
         List<SpecParam> specParams = this.service.queryParams(gid,cid,generic,searching);
           if(CollectionUtils.isEmpty(specParams))
               return  ResponseEntity.notFound().build();
           return  ResponseEntity.ok(specParams);
     }

//    @GetMapping("params")
//    public ResponseEntity<List<SpecParam>> queryParamsByCID(@RequestParam(name="cid")Long cid){
//        List<SpecParam> specParams = this.service.queryParamsByCID(cid);
//        if(CollectionUtils.isEmpty(specParams))
//            return  ResponseEntity.notFound().build();
//        return  ResponseEntity.ok(specParams);
//    }
     @PostMapping("group")
    public  ResponseEntity<Void> saveGroupByCid(SpecGroup specGroup){
         this.service.saveGroupByCid(specGroup);
         return ResponseEntity.ok().build();
     }
    @PostMapping("param")
    public  ResponseEntity<Void> saveParam(SpecParam specParam){
        this.service.saveParam(specParam);
        return ResponseEntity.ok().build();
    }
    @PutMapping("param")
    public  ResponseEntity<Void> updateParam(SpecParam specParam){
        this.service.updateParam(specParam);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("param/{id}")
    public  ResponseEntity<Void> deleteParam(@PathVariable(name="id")Long id){
        this.service.deleteParam(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("group")
    public  ResponseEntity<Void> updateGroup(SpecGroup specGroup){
        this.service.updateGroup(specGroup);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("group/{gid}")
    public  ResponseEntity<Void> deleteGroup(@PathVariable(name="gid")Long gid){
         this.service.deleteGroup(gid);
         return  ResponseEntity.ok().build();
    }

    @GetMapping("{cid}")
    public  ResponseEntity<List<SpecGroup>> querySpecByCid(@PathVariable(name = "cid") Long cid){
        List<SpecGroup> specGroup= this.service.querySpecByCid(cid);
        if(CollectionUtils.isEmpty(specGroup)){
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(specGroup);
    }
}

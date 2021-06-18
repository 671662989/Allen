package com.leyou.item.service;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> queryGroupsByPID(Long pid) {
        SpecGroup specGroup=new SpecGroup();
        specGroup.setCid(pid);
        return this.specGroupMapper.select(specGroup);
    }

    public List<SpecParam> queryParamsByGID(Long gid) {
        SpecParam specParam=new SpecParam();
        specParam.setGroupId(gid);
        return this.specParamMapper.select(specParam);
    }

    public void saveGroupByCid(SpecGroup specGroup) {
        this.specGroupMapper.insert(specGroup);
    }

    public void updateGroup(SpecGroup specGroup) {
        this.specGroupMapper.updateByPrimaryKey(specGroup);
    }


    public void deleteGroup(Long gid) {
        this.specParamMapper.deleteByGid(gid);
        this.specGroupMapper.deleteByPrimaryKey(gid);

    }

    public void saveParam(SpecParam specParam) {
        this.specParamMapper.insert(specParam);
    }

    public void updateParam(SpecParam specParam) {
        this.specParamMapper.updateByPrimaryKey(specParam);
    }

    public void deleteParam(Long id) {
        this.specParamMapper.deleteByPrimaryKey(id);
    }

    public List<SpecParam> queryParamsByCID(Long cid) {
        SpecParam specParam = new SpecParam();
        specParam.setCid(cid);
        return this.specParamMapper.select(specParam);
    }

    public List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching) {
      SpecParam specParam=new SpecParam();
        specParam.setCid(cid);
        specParam.setGroupId(gid);
        specParam.setGeneric(generic);
        specParam.setSearching(searching);
        return  this.specParamMapper.select(specParam);
    }

    public List<SpecGroup> querySpecByCid(Long cid) {
        List<SpecGroup> specGroups = this.queryGroupsByPID(cid);
        specGroups.forEach(specGroup -> {
            List<SpecParam> specParams = this.queryParamsByGID(specGroup.getId());
            specGroup.setParams(specParams);
        });

     return  specGroups;
    }
}

package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoriesByPid(Long pid) {
        Category record=new Category();
        record.setParentId(pid);
       return this.categoryMapper.select(record);
    }

    public List<Category> queryCategoriesByBid(Long bid) {

       return  this.categoryMapper.queryCategoriesByBid(bid);

    }

    public List<String> queryNamesById(List<Long> ids) {
       List<Category> categories=this.categoryMapper.selectByIdList(ids);
       List<String>  names=new ArrayList<>();
        for (Category category:categories) {
            names.add(category.getName());

        }
        return names;
    }

    public List<Category> queryAllLevel(Long cid3) {

        Category  c3 = this.categoryMapper.selectByPrimaryKey(cid3);
        Category c2 = this.categoryMapper.selectByPrimaryKey(c3.getParentId());
        Category c1 = this.categoryMapper.selectByPrimaryKey(c2.getParentId());
        return Arrays.asList(c1,c2,c3);
    }
}

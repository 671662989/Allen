package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

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
public interface CategoryMapper  extends Mapper<Category> ,SelectByIdListMapper<Category,Long> {
    @Select("SELECT * FROM TB_CATEGORY T  WHERE T.ID IN (SELECT CATEGORY_ID FROM TB_CATEGORY_BRAND WHERE BRAND_ID=#{bid})")
    List<Category> queryCategoriesByBid(Long bid);
}

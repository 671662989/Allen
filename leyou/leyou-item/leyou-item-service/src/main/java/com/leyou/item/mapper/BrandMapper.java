package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
public interface BrandMapper extends Mapper<Brand> {

    @Insert("INSERT INTO tb_category_brand(category_id,brand_id) VALUES(#{cid},#{bid})")
    int insertBrandAndCategory(@Param("cid")Long cid, @Param("bid")Long bid);
    @Delete("DELETE FROM tb_category_brand  WHERE BRAND_ID=#{bid}")
    void deleteCategoryByBrandId(Long bid);
    @Select("SELECT * FROM TB_BRAND T,TB_CATEGORY_BRAND R WHERE T.ID=R.BRAND_ID AND R." +
            "CATEGORY_ID=#{cid}")
    List<Brand> selectBrandByCid(Long cid);
}

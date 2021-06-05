package com.leyou.item.mapper;

import com.leyou.item.pojo.SpecParam;
import org.apache.ibatis.annotations.Delete;
import tk.mybatis.mapper.common.Mapper;

/**
 * create by: cdy
 * description:$
 * create time: $ $
 * <p>
 * $
 *
 * @return $
 */
public interface SpecParamMapper extends Mapper<SpecParam> {
    @Delete("DELETE FROM tb_spec_param WHERE GROUP_ID=#{gid}")
    void deleteByGid(Long gid);
}

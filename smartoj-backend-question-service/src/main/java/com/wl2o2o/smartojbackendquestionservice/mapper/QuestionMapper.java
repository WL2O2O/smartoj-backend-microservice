package com.wl2o2o.smartojbackendquestionservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wl2o2o.smartojbackendmodel.model.entity.Question;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Entity com.wl2o2o.smartoj.model.entity.Question
 */
public interface QuestionMapper extends BaseMapper<Question> {

    @Select("SELECT id FROM question")
    List<Long> selectIds();
}





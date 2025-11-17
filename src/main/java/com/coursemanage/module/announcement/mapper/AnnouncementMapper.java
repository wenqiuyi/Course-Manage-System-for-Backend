package com.coursemanage.module.announcement.mapper;

import com.coursemanage.module.announcement.pojo.AnnouncementEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AnnouncementMapper {
    @Select("select * from announcement")
    List<AnnouncementEntity> select();
    @Select("select * from announcement where id = #{id}")
    AnnouncementEntity selectById(Long id);
    @Insert("insert into announcement(title, content, publish_time, deadline, level, status)" +
            "values(#{title},#{content},#{publishTime},#{deadline},#{level},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AnnouncementEntity announcementEntity);
    @Update("UPDATE announcement SET " +
            "title = #{title}, " +
            "content = #{content}, " +
            "publish_time = #{publishTime}, " +
            "deadline = #{deadline}, " +
            "level = #{level}, " +
            "status = #{status} " +
            "WHERE id = #{id}")
    int updateById(AnnouncementEntity announcementEntity);
    @Delete("DELETE FROM announcement WHERE id = #{id}")
    int deleteById(Long id);
}

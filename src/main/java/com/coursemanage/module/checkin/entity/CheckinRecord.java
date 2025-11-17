package com.coursemanage.module.checkin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("checkin_record")
public class CheckinRecord {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer checkinId;
    private String studentNo;
    /**
     * 1=已签到 0=缺勤
     */
    private Boolean status;
    private LocalDateTime checkinTime;
}


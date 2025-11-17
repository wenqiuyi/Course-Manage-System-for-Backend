package com.coursemanage.module.log.pojo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogEntity {
    private Long id;
    @JsonProperty("operation_time")
    private Date operationTime;
    @JsonProperty("operator_school_num")
    private String operatorSchoolNum;
    @JsonProperty("target_school_num")
    private String targetSchoolNum;
    @JsonProperty("detail_info")
    private String detailInfo;
    @JsonProperty("operation_module")
    private String operationModule;
    @JsonProperty("operation_action")
    private String operationAction;
}

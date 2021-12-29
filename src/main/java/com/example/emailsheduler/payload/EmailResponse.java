package com.example.emailsheduler.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
public class EmailResponse {

    private boolean success;

    // for unique job
    private String jobId;

    // for unique job
    private String jobGroup;

    private String message;

    public EmailResponse(boolean success,String message){
        this.success = success;
        this.message = message;
    }
    // comment explain the why it is writed, not what writed
    // why write this code? this constructor help 'something'
    public EmailResponse(boolean success,String jobId,String jobGroup,String message){
        this.success = success;
        this.jobId = jobId;
        this.jobGroup = jobGroup;
        this.message = message;
    }
}

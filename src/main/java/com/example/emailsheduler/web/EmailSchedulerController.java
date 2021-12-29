package com.example.emailsheduler.web;

import com.example.emailsheduler.payload.EmailRequest;
import com.example.emailsheduler.payload.EmailResponse;
import com.example.emailsheduler.quartz.job.EmailJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * package 'web' : end-point for this application
 */
@Slf4j
@RestController
public class EmailSchedulerController {

    @Autowired
    private Scheduler scheduler;

    @PostMapping("/schedule/email")
    public ResponseEntity<EmailResponse> scheduleEmail(@Valid @RequestBody EmailRequest emailRequest){
        try{
            ZonedDateTime dateTime = ZonedDateTime.of(emailRequest.getDateTime(), emailRequest.getTimeZone());
            if(dateTime.isBefore(ZonedDateTime.now())){
                EmailResponse emailResponse = new EmailResponse(false,"dateTime must be after currenty time");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(emailResponse);
            }

            JobDetail jobDetail =  buildJobDetail(emailRequest);
            Trigger trigger = buildTrigger(jobDetail,dateTime);
            scheduler.scheduleJob(jobDetail,trigger); //scheduler 소스 한번 보기

            EmailResponse emailResponse = new EmailResponse(true,jobDetail.getKey().getName(),jobDetail.getKey().getGroup(),"Email Scheduled Success");
            return ResponseEntity.status(HttpStatus.OK).body(emailResponse);

        }catch(SchedulerException se){
            log.error("Error while schduling email: ", se);
            EmailResponse emailResponse = new EmailResponse(false, "Error, try again");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emailResponse);
        }

    }

    @GetMapping("/get")
    public ResponseEntity<String> get(){
        return ResponseEntity.ok("check the app -- pass");
    }

    private JobDetail buildJobDetail(EmailRequest scheduleEmailRequest){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("email", scheduleEmailRequest.getEmail());
        jobDataMap.put("subject", scheduleEmailRequest.getSubject());
        jobDataMap.put("body", scheduleEmailRequest.getBody());


        return JobBuilder.newJob(EmailJob.class).withIdentity(UUID.randomUUID().toString(),"email-job")
                .usingJobData(jobDataMap) //store job even without trigger
                .storeDurably().build();
    }

    private Trigger buildTrigger(JobDetail jobDetail, ZonedDateTime startAt){
        return TriggerBuilder.newTrigger().withIdentity(jobDetail.getKey().getName(),"email-trigger")
                .withDescription("send Email Trigger")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}

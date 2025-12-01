package org.example.c8.workers;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import io.camunda.client.api.response.ActivatedJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.example.c8.Application.isLogJobEnabled;
import static org.example.c8.utilities.Loggers.*;

@Component
@Slf4j
public class GetVariables3 {

    // See JavaTimeConfigurator, this method signature is compatible with objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    @JobWorker(type = "getVariables3")
    public void handleGetVariables3(final ActivatedJob job, @Variable Boolean aBoolean1, @Variable String aDate1) {
        String methodName = "handleGetVariables3";

        if (log.isDebugEnabled()) logDebugEnterJob(methodName, job);
        if (isLogJobEnabled) logJob(methodName, job, null);

        if (log.isDebugEnabled()) log.debug("-----> {}: aBoolean1 = {}, aDate1 = {}", methodName, aBoolean1, aDate1);

        if (log.isDebugEnabled()) logDebugExitJob(methodName, job);
    }

//    // See JavaTimeConfigurator, this method signature is compatible with objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
//    @JobWorker(type = "getVariables3")
//    public void handleGetVariables3(final ActivatedJob job, @Variable Boolean aBoolean1, @Variable Long aDate1) {
//        String methodName = "handleGetVariables3";
//
//        if (log.isDebugEnabled()) log.debug("-----> {}: Enter", methodName);
//        if (Application.isLogJobEnabled) Application.logJob(methodName, job, null);
//
//        if (log.isDebugEnabled()) log.debug("-----> {}: aBoolean1 = {}, aDate1 = {}", methodName, aBoolean1, new Date(aDate1));
//
//        if (log.isDebugEnabled()) log.debug("-----> {}: Exit", methodName);
//    }
}

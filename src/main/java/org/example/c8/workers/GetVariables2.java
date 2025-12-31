package org.example.c8.workers;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.example.c8.Application.isLogJobEnabled;
import static org.example.c8.utilities.Loggers.*;

@Component
@Slf4j
public class GetVariables2 {

    @JobWorker(type = "getVariables2")
    public void handleGetVariables2(final ActivatedJob job) {
        final String methodName = "handleGetVariables2";

        if (log.isDebugEnabled()) log.debug("-----> {}: Enter job {} of instance {}",  methodName, job.getKey(), job.getProcessInstanceKey());
        if (isLogJobEnabled) logJob(methodName, job, null);

        Long aLong1 = (Long)job.getVariablesAsMap().get("aLong1");
        Double aDouble1 = (Double)job.getVariablesAsMap().get("aDouble1");
        if (log.isDebugEnabled()) log.debug("-----> {}: aLong1 = {}, aDouble1 = {}", methodName, aLong1, aDouble1);

        if (log.isDebugEnabled()) log.debug("-----> {}: Exit job {} of instance {}",  methodName, job.getKey(), job.getProcessInstanceKey());
    }
}

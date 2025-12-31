package org.example.c8.workers;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.example.c8.Application.isLogJobEnabled;
import static org.example.c8.utilities.Loggers.*;

@Component
@Slf4j
public class SetVariables2 {

    @JobWorker(type = "setVariables2")
    public Map<String, Object> handleSetVariables2(final ActivatedJob job) {
        final String methodName = "handleSetVariables2";

        if (log.isDebugEnabled()) log.debug("-----> {}: Enter job {} of instance {}",  methodName, job.getKey(), job.getProcessInstanceKey());
        if (isLogJobEnabled) logJob(methodName, job, null);

        Map<String, Object> variablesMap = new HashMap<>();
        variablesMap.put("aLong1", 21474836478L);
        variablesMap.put("aDouble1", 10293.84756D);

        if (log.isDebugEnabled()) log.debug("-----> {}: Exit job {} of instance {}",  methodName, job.getKey(), job.getProcessInstanceKey());
        return variablesMap;
    }
}

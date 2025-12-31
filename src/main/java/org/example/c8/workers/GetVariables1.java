package org.example.c8.workers;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.example.c8.Application.isLogJobEnabled;
import static org.example.c8.utilities.Loggers.*;

@Component
@Slf4j
public class GetVariables1 {
    @JobWorker(type = "getVariables1", autoComplete = false)
    public void handleGetVariables1(final JobClient client, final ActivatedJob job) {
        final String methodName = "handleGetVariables1";

        if (log.isDebugEnabled()) log.debug("-----> {}: Enter job {} of instance {}",  methodName, job.getKey(), job.getProcessInstanceKey());
        if (isLogJobEnabled) logJob(methodName, job, null);

        Map<String, Object> variables = job.getVariablesAsMap();
        Integer anInteger1 = (Integer)variables.get("anInteger1");
        String aString1 = (String)variables.get("aString1");
        if (log.isDebugEnabled()) log.debug("-----> {}: anInteger1 = {}, aString1 = {}", methodName, anInteger1, aString1);

        // This is useful for when special handling of successful and / or unsuccessful job completion is necessary.
        // To use this, "autoComplete = false" must be set in the @JobWorker annotation.
        client.newCompleteCommand(job.getKey())
                .send().whenComplete((result, exception) -> {
                    if (exception == null) {
                        if (log.isDebugEnabled()) log.debug("-----> {}: Completed job successfully", methodName);
                    } else {
                        log.error("-----> {}: Failed to complete job", methodName, exception);
                    }
                });

        if (log.isDebugEnabled()) log.debug("-----> {}: Exit job {} of instance {}",  methodName, job.getKey(), job.getProcessInstanceKey());
    }
}

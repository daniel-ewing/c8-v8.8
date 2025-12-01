package org.example.c8.workers;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.example.c8.Application.isLogJobEnabled;
import static org.example.c8.utilities.Loggers.*;

@Component
@Slf4j
public class SetVariables1 {

    @JobWorker(type = "setVariables1", autoComplete = false)
    public void handleSetVariables1(final JobClient client, final ActivatedJob job) {
        String methodName = "handleSetVariables1";

        if (log.isDebugEnabled()) logDebugEnterJob(methodName, job);
        if (isLogJobEnabled) logJob(methodName, job, null);

        String variables =
                "{" +
                "\"anInteger1\": 1," +
                "\"aString1\": \"string1\"" +
                "}";

        // This is useful for when special handling of successful and / or unsuccessful job completion is necessary.
        // To use this, "autoComplete = false" must be set in the @JobWorker annotation.
        client.newCompleteCommand(job.getKey())
                .variables(variables).send().whenComplete((result, exception) -> {
                    if (exception == null) {
                        if (log.isDebugEnabled()) log.debug("-----> {}: Completed job successfully", methodName);
                    } else {
                        log.error("-----> {}: Failed to complete job", methodName, exception);
                    }
                });

        if (log.isDebugEnabled()) logDebugExitJob(methodName, job);
    }
}

package org.example.workers;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import lombok.extern.slf4j.Slf4j;
import org.example.model.PersonInformation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.example.Application.isLogJobEnabled;
import static org.example.utilities.Loggers.logJob;

@Component
@Slf4j
public class testWorker1 {
    @JobWorker(type = "testWorker1")
    public Map<String, Object>  handleGetVariables1(final JobClient client, final ActivatedJob job) {
        final String methodName = "testWorker1";

        if (log.isDebugEnabled()) log.debug("-----> {}: Enter job {} of instance {}",  methodName, job.getKey(), job.getProcessInstanceKey());
        if (isLogJobEnabled) logJob(methodName, job, null);

        Map<String, Object> variablesMap = new HashMap<>();
        variablesMap.put("newUuid", UUID.randomUUID());
        variablesMap.put("information", new PersonInformation(
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1)
                ));

        if (log.isDebugEnabled()) log.debug("-----> {}: Exit job {} of instance {}",  methodName, job.getKey(), job.getProcessInstanceKey());
        return variablesMap;
    }
}

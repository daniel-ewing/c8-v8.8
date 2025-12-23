package org.example.c8.utilities;

import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.response.ProcessInstanceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
public class Loggers {

    public static void logDebugEnter(String methodName)
    {
        log.debug("-----> {}: Enter", methodName);
    }

    public static void logDebugEnterJob(String methodName, ActivatedJob job)
    {
        log.debug("-----> {}: Enter job {} of instance {}",  methodName, job.getKey(), job.getProcessInstanceKey());
    }

    public static void logDebugExit(String methodName)
    {
        log.debug("-----> {}: Exit", methodName);
    }

    public static void logDebugExitJob(String methodName, ActivatedJob job)
    {
        log.debug("-----> {}: Exit job {} of instance {}",  methodName, job.getKey(), job.getProcessInstanceKey());
    }

    public static void logJob(final String caller, final ActivatedJob job, Object parameterValue) {
        log.info("""
                        -----> {}: logJob:
                        [type: {}, key: {}, element: {}, workflow instance: {}, deadline: {}]
                        [headers: {}]
                        [variables: {}]
                        [parameter: {}]""",
                caller,
                job.getType(),
                job.getKey(),
                job.getElementId(),
                job.getProcessInstanceKey(),
                Instant.ofEpochMilli(job.getDeadline()),
                job.getCustomHeaders(),
                job.getVariables(),
                parameterValue);
    }

    public static void logInstance(final String caller, ProcessInstanceEvent event){
        if (log.isDebugEnabled()) log.debug(
                "-----> {}: started instance for processDefinitionKey='{}', bpmnProcessId='{}', version='{}' with processInstanceKey='{}'",
                caller,
                event.getProcessDefinitionKey(),
                event.getBpmnProcessId(),
                event.getVersion(),
                event.getProcessInstanceKey());
    }

    public static void logInstancesCreatedCount(final String caller, int piCount){
        if (log.isDebugEnabled()) log.debug("-----> {}: created {} process instances", caller, piCount);
    }
}

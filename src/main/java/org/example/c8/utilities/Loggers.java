package org.example.c8.utilities;

import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.response.ProcessInstanceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static org.example.c8.configuration.JavaTimeConfigurator.getDateTime;

@Component
@Slf4j
public class Loggers {

    public static void logDebugEnter(String methodName)
    {
        log.debug("{} -----> {}: Enter", getDateTime(), methodName);
    }

    public static void logDebugEnterJob(String methodName, ActivatedJob job)
    {
        log.debug("{} -----> {}: Enter job {} of instance {}",  getDateTime(), methodName, job.getKey(), job.getProcessInstanceKey());
    }

    public static void logDebugExit(String methodName)
    {
        log.debug("{} -----> {}: Exit", getDateTime(), methodName);
    }

    public static void logDebugExitJob(String methodName, ActivatedJob job)
    {
        log.debug("{} -----> {}: Exit job {} of instance {}",  getDateTime(), methodName, job.getKey(), job.getProcessInstanceKey());
    }

    public static void logJob(final String caller, final ActivatedJob job, Object parameterValue) {
        log.info("""
                        {} -----> {}: logJob:
                        [type: {}, key: {}, element: {}, workflow instance: {}, deadline: {}]
                        [headers: {}]
                        [variables: {}]
                        [parameter: {}]""",
                getDateTime(),
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
                "{} -----> {}: started instance for workflowKey='{}', bpmnProcessId='{}', version='{}' with workflowInstanceKey='{}'",
                getDateTime(),
                caller,
                event.getProcessDefinitionKey(),
                event.getBpmnProcessId(),
                event.getVersion(),
                event.getProcessInstanceKey());
    }
}

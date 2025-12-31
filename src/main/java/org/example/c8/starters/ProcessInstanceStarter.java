package org.example.c8.starters;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.ProcessInstanceEvent;
import lombok.extern.slf4j.Slf4j;
import org.example.c8.utilities.VariableGenerator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletionStage;

import static org.example.c8.Application.*;

@Component
@EnableScheduling
@Slf4j
public class ProcessInstanceStarter {
    private static CamundaClient client;

    public ProcessInstanceStarter(CamundaClient client) {
        ProcessInstanceStarter.client = client;
    }

    @Scheduled(fixedRate = 60000L)
    public void startProcessInstancePeriodic(){
        final String methodName = "startProcessInstancePeriodic";

        if (!isStartInstancesByPeriod) return;
        if (log.isDebugEnabled()) log.debug("-----> {}: Enter", methodName);

        if (isStartInstancesAsynchronous) {
            startProcessInstanceAsynchronous(defaultProcessKey);
        } else {
            startProcessInstanceSynchronous(defaultProcessKey);
        }

        if (log.isDebugEnabled()) log.debug("-----> {}: Exit", methodName);
    }

    public static void startProcessInstances(int instanceCount, boolean isAsynchronous){
        startProcessInstances(defaultProcessKey, instanceCount, isAsynchronous);
    }

    public static void startProcessInstances(String processKey, int instanceCount, boolean isAsynchronous){
        final String methodName = "startProcessInstances";

        if (log.isDebugEnabled()) log.debug("-----> {}: Enter", methodName);

        for (int piCount = 1; piCount <= instanceCount; piCount++) {

            if (isAsynchronous) {
                startProcessInstanceAsynchronous(processKey);
            } else {
                startProcessInstanceSynchronous(processKey);
            }

            if ((piCount % 1000) == 0) {
                if (log.isDebugEnabled()) log.debug("-----> {}: created {} process instances", methodName, piCount);
            }
        }
        if (log.isDebugEnabled()) log.debug("-----> {}: Exit", methodName);
    }

    public static void startProcessInstanceAsynchronous(String processKey){
        final String methodName = "startProcessInstanceAsynchronous";

        if (log.isDebugEnabled()) log.debug("-----> {}: Enter", methodName);

        CompletionStage<ProcessInstanceEvent> completionStage = client.newCreateInstanceCommand()
                .bpmnProcessId(processKey)
                .latestVersion()
                .variables(VariableGenerator.generateSimpleVariables())
                .send()
                .whenCompleteAsync((result, exception) -> {
                    if (exception == null) {
                        if (isLogInstanceEnabled) log.info(
                                "-----> {}: started instance for processDefinitionKey='{}', bpmnProcessId='{}', version='{}' with processInstanceKey='{}'",
                                methodName,
                                result.getProcessDefinitionKey(),
                                result.getBpmnProcessId(),
                                result.getVersion(),
                                result.getProcessInstanceKey());
                    } else {
                        log.error("-----> {}: Failed to created instance of {}", methodName, processKey, exception);
                    }
                });

        if (log.isDebugEnabled()) log.debug("-----> {}: Exit", methodName);
    }

    public static void startProcessInstanceSynchronous(String processKey){
        final String methodName = "startProcessInstanceSynchronous";

        if (log.isDebugEnabled()) log.debug("-----> {}: Enter", methodName);

        ProcessInstanceEvent processInstanceEvent = client.newCreateInstanceCommand()
                .bpmnProcessId(processKey)
                .latestVersion()
                .variables(VariableGenerator.generateSimpleVariables())
                .execute();

        if (isLogInstanceEnabled) log.debug(
                "-----> {}: started instance for processDefinitionKey='{}', bpmnProcessId='{}', version='{}' with processInstanceKey='{}'",
                methodName,
                processInstanceEvent.getProcessDefinitionKey(),
                processInstanceEvent.getBpmnProcessId(),
                processInstanceEvent.getVersion(),
                processInstanceEvent.getProcessInstanceKey());

        if (log.isDebugEnabled()) log.debug("-----> {}: Exit", methodName);
    }
}

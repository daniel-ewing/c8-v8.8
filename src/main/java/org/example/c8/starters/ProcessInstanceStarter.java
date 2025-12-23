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
import static org.example.c8.Application.processKey;
import static org.example.c8.utilities.Loggers.*;

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
        String methodName = "startProcessInstancePeriodic";

        if (!isStartInstancesByPeriodEnabled) return;
        if (log.isDebugEnabled()) logDebugEnter(methodName);

        if (isStartInstancesAsynchronous) {
            startProcessInstanceAsynchronous();
        } else {
            startProcessInstanceSynchronous();
        }

        if (log.isDebugEnabled()) logDebugExit(methodName);
    }

    public static void startProcessInstances(int instanceCount){
        String methodName = "startProcessInstances";

        if (!isStartInstancesByCounterEnabled) return;
        if (log.isDebugEnabled()) logDebugEnter(methodName);

        for (int piCount = 1; piCount <= instanceCount; piCount++) {

            if (isStartInstancesAsynchronous) {
                startProcessInstanceAsynchronous();
            } else {
                startProcessInstanceSynchronous();
            }

            if ((piCount % 1000) == 0) {
                if (log.isDebugEnabled()) logInstancesCreatedCount(methodName, piCount);
            }
        }
        if (log.isDebugEnabled()) logDebugExit(methodName);
    }

    public static void startProcessInstanceAsynchronous(){
        String methodName = "startProcessInstanceAsynchronous";

        if (log.isDebugEnabled()) logDebugEnter(methodName);

        CompletionStage<ProcessInstanceEvent> completionStage = client.newCreateInstanceCommand()
                .bpmnProcessId(processKey)
                .latestVersion()
                .variables(VariableGenerator.generateSimpleVariables())
                .send()
                .whenCompleteAsync((result, exception) -> {
                    if (exception == null) {
                        if (isLogInstanceEnabled) logInstance(methodName, result);
                    } else {
                        log.error("-----> {}: Failed to created instance of {}", methodName, processKey, exception);
                    }
                });

        if (log.isDebugEnabled()) logDebugExit(methodName);
    }

    public static void startProcessInstanceSynchronous(){
        String methodName = "startProcessInstanceSynchronous";

        if (log.isDebugEnabled()) logDebugEnter(methodName);

        ProcessInstanceEvent processInstanceEvent = client.newCreateInstanceCommand()
                .bpmnProcessId(processKey)
                .latestVersion()
                .variables(VariableGenerator.generateSimpleVariables())
                .send()
                .join();

        if (log.isDebugEnabled()) logDebugExit(methodName);
    }
}

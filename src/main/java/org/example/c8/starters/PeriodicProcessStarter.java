package org.example.c8.starters;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.ProcessInstanceEvent;
import lombok.extern.slf4j.Slf4j;
import org.example.c8.Application;
import org.example.c8.utilities.VariableGenerator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static org.example.c8.utilities.Loggers.*;

@Component
@EnableScheduling
@Slf4j
public class PeriodicProcessStarter {

    private final CamundaClient client;

    public PeriodicProcessStarter(CamundaClient client) {
        this.client = client;
    }


    @Scheduled(fixedRate = 60000L)
    public void startProcessInstance(){
        String methodName = "startProcessInstance";

        if (log.isDebugEnabled()) logDebugEnter(methodName);
        if (!Application.isPeriodicProcessStarterEnabled) return;

        // blocking / synchronous creation of a process instance => returns an instance
        ProcessInstanceEvent processInstanceEvent = client.newCreateInstanceCommand()
                .bpmnProcessId(Application.processKey)
                .latestVersion()
                .variables(VariableGenerator.generateSimpleVariables())
                .send()
                .join();

        if (log.isDebugEnabled()) logInstance(methodName, processInstanceEvent);

        if (log.isDebugEnabled()) logDebugExit(methodName);
    }

}

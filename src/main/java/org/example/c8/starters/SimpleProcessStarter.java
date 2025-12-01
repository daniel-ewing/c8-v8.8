package org.example.c8.starters;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.ProcessInstanceEvent;
import lombok.extern.slf4j.Slf4j;
import org.example.c8.utilities.VariableGenerator;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletionStage;

import static org.example.c8.Application.*;
import static org.example.c8.configuration.JavaTimeConfigurator.getDateTime;
import static org.example.c8.utilities.Loggers.*;

@Component
@Slf4j
public class SimpleProcessStarter {

    public static void startProcessInstanceAsynchronous(CamundaClient client, int instanceCount){
        String methodName = "startProcessInstanceAsynchronous";

        if (log.isDebugEnabled()) logDebugEnter(methodName);
        if (!isSimpleProcessStarterAsynchronousEnabled) return;

        for (int pi = 1; pi <= instanceCount; pi++) {
            CompletionStage<ProcessInstanceEvent> completionStage = client.newCreateInstanceCommand()
                    .bpmnProcessId(processKey)
                    .latestVersion()
                    .variables(VariableGenerator.generateSimpleVariables())
                    .send()
                    .whenCompleteAsync((result, exception) -> {
                        if (exception == null) {
                            if (log.isDebugEnabled()) logInstance(methodName, result);
                        } else {
                            log.error("{} -----> {}: Failed to created instance of {}", getDateTime() , methodName, processKey, exception);
                        }
                    });


            if ((pi % 1000) == 0) {
                if (log.isDebugEnabled()) log.debug("{} -----> {}: created {} process instances", getDateTime(), methodName, pi);
            }
        }

        if (log.isDebugEnabled()) logDebugExit(methodName);
    }

    public static void startProcessInstanceSynchronous(CamundaClient client, int instanceCount){
        String methodName = "startProcessInstanceSynchronous";

        if (log.isDebugEnabled()) logDebugEnter(methodName);
        if (!isSimpleProcessStarterSynchronousEnabled) return;

        for (int pi = 1; pi <= instanceCount; pi++) {
            ProcessInstanceEvent processInstanceEvent = client.newCreateInstanceCommand()
                    .bpmnProcessId(processKey)
                    .latestVersion()
                    .variables(VariableGenerator.generateSimpleVariables())
                    .send()
                    .join();

            if ((pi % 1000) == 0) {
                if (log.isDebugEnabled()) log.debug("{} -----> {}: created {} process instances", getDateTime(), methodName, pi);
            }

            if (log.isDebugEnabled()) logInstance(methodName, processInstanceEvent);
        }

        if (log.isDebugEnabled()) logDebugExit(methodName);
    }
}

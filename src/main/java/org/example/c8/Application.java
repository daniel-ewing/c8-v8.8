package org.example.c8;

import io.camunda.client.CamundaClient;
import io.camunda.client.annotation.Deployment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import static org.example.c8.starters.SimpleProcessStarter.startProcessInstanceAsynchronous;
import static org.example.c8.starters.SimpleProcessStarter.startProcessInstanceSynchronous;

@SpringBootApplication
@Slf4j
@Deployment(resources = "classpath*:/bpmn/**/*.bpmn")
public class Application {
    public final static boolean isLogJobEnabled = true;
    public final static boolean isPeriodicProcessStarterEnabled = false;
    public final static boolean isSimpleProcessStarterSynchronousEnabled = false;
    public final static boolean isSimpleProcessStarterAsynchronousEnabled = true;
//    public final static String processKey = "process";
    public final static String processKey = "simple-variables";
    private final CamundaClient client;

    public Application(CamundaClient client) {
        this.client = client;
    }

    public static void main(final String... args) {
        SpringApplication.run(Application.class, args);
    }


    @EventListener
    private void ApplicationReadyEvent(ApplicationReadyEvent event) {
        if (log.isDebugEnabled()) log.debug("-----> ApplicationReadyEvent: Enter");
        if (isSimpleProcessStarterAsynchronousEnabled) startProcessInstanceAsynchronous(client, 1);
        if (isSimpleProcessStarterSynchronousEnabled) startProcessInstanceSynchronous(client, 1);
    }
}
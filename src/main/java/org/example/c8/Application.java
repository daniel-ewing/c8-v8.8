package org.example.c8;

import io.camunda.client.annotation.Deployment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import static org.example.c8.starters.ProcessInstanceStarter.startProcessInstances;

@SpringBootApplication
@Slf4j
@Deployment(resources = "classpath*:/bpmn/**/*.bpmn")
public class Application {
    public final static boolean isLogInstanceEnabled = true;
    public final static boolean isLogJobEnabled = false;
    public final static boolean isStartInstancesByPeriodEnabled = false;
    public final static boolean isStartInstancesByCounterEnabled = true;
    public final static boolean isStartInstancesAsynchronous = true;
//    public final static String processKey = "process";
    public final static String processKey = "simple-variables";

    public static void main(final String... args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener
    private void ApplicationReadyEvent(ApplicationReadyEvent event) {
        if (log.isDebugEnabled()) log.debug("-----> ApplicationReadyEvent: Enter");
        if (isStartInstancesByCounterEnabled) startProcessInstances(1);
    }
}
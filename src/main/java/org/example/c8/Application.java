package org.example.c8;

import io.camunda.client.annotation.Deployment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import static org.example.c8.starters.DataGenerator.deployModels;
import static org.example.c8.starters.ProcessInstanceStarter.startProcessInstances;

@SpringBootApplication
@Slf4j
@Deployment(resources = "classpath*:/bpmn/**/*.bpmn")
public class Application {
    public final static boolean isLogInstanceEnabled = false;
    public final static boolean isLogJobEnabled = false;
    public final static boolean isStartInstancesByPeriod = false;
    public final static boolean isStartInstancesByCounter = true;
    public final static boolean isStartInstancesByDataGenerator = false;
    public final static boolean isStartInstancesAsynchronous = true;
//    public final static String defaultProcessKey = "process";
    public final static String defaultProcessKey = "simple-variables";

    public static void main(final String... args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener
    private void ApplicationReadyEvent(ApplicationReadyEvent event) {
        final String methodName = "ApplicationReadyEvent";

        if (log.isDebugEnabled()) log.debug("-----> {}: Enter", methodName);

        if (isStartInstancesByCounter) startProcessInstances(3, isStartInstancesAsynchronous);
        if (isStartInstancesByDataGenerator) deployModels(5, 5);

        if (log.isDebugEnabled()) log.debug("-----> {}: Exit", methodName);
    }
}
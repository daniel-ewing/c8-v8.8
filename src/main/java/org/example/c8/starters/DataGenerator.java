package org.example.c8.starters;

import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.DeploymentEvent;
import io.camunda.zeebe.model.bpmn.Bpmn;
import io.camunda.zeebe.model.bpmn.BpmnModelInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.example.c8.Application.isStartInstancesAsynchronous;
import static org.example.c8.starters.ProcessInstanceStarter.startProcessInstances;

@Component
@Slf4j
public class DataGenerator {
    private static CamundaClient client;

    public DataGenerator(CamundaClient client) {
        DataGenerator.client = client;
    }

    public static void deployModels(int numberOfModels, int numberOfInstancesPerModel) {
        final String methodName = "deployModels";
        final String processPrefix = "process";
        final String decisionPrefix = "decision";
        final String decisionNamePrefix = "Decision Task ";

        if (log.isDebugEnabled()) log.debug("-----> {}: Enter", methodName);

        for (int modelCount = 1; modelCount <= numberOfModels; modelCount++) {
            final String processId = processPrefix + modelCount;
            final String decisionId = decisionPrefix + modelCount;
            final String decisionName = decisionNamePrefix + modelCount;

            if (log.isDebugEnabled()) log.debug("-----> {}: Deploying processId {} and decisionId {}", methodName, processId, decisionId);

            // create a process which evaluates 1 decision and has 1 service task
            final BpmnModelInstance processModel = Bpmn.createExecutableProcess(processId)
                    .startEvent()
                    .businessRuleTask(
                            "decisionTask" + modelCount,
                            t -> t.name(decisionName).zeebeCalledDecisionId(decisionId).zeebeResultVariable("result"))
                    .serviceTask(
                            "setVariables1",
                            t -> t.name("Set Variables 1").zeebeJobType("setVariables1")
                    )
                    .endEvent()
                    .done();

            // a minimal decision definition that can be deployed with the process
            final String decisionModel =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                            + "<definitions xmlns=\"https://www.omg.org/spec/DMN/20191111/MODEL/\" "
                            + "xmlns:feel=\"https://www.omg.org/spec/DMN/20191111/FEEL/\" "
                            + "id=\"definitions_"
                            + decisionId
                            + "\" "
                            + "name=\"definitions_"
                            + decisionId
                            + "\" "
                            + "namespace=\"http://camunda.org/schema/1.0/dmn\">"
                            + "<decision id=\""
                            + decisionId
                            + "\" name=\""
                            + decisionId
                            + "\">"
                            + "<decisionTable id=\"decisionTable_"
                            + decisionId
                            + "\">"
                            + "<output id=\"output_"
                            + decisionId
                            + "\" name=\"result\" typeRef=\"string\"/>"
                            + "<rule id=\"rule_"
                            + decisionId
                            + "\">"
                            + "<outputEntry id=\"outputEntry_"
                            + decisionId
                            + "\"><text>\"ok\"</text></outputEntry>"
                            + "</rule>"
                            + "</decisionTable>"
                            + "</decision>"
                            + "</definitions>";

            final DeploymentEvent deploymentEvent = client.newDeployResourceCommand()
                    .addProcessModel(processModel, processId + ".bpmn")
                    .addResourceBytes(decisionModel.getBytes(), decisionId + ".dmn")
                    .execute();

            startProcessInstances(processId, numberOfInstancesPerModel, isStartInstancesAsynchronous);
        }
        if (log.isDebugEnabled()) log.debug("-----> {}: Exit", methodName);
    }
}

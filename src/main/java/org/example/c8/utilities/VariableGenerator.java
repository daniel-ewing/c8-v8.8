package org.example.c8.utilities;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class VariableGenerator {
    public static Map<String, Object> generateSimpleVariables() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("uuid", UUID.randomUUID().toString());
        variables.put("startDateTime", new Date());
        return variables;
    }

}

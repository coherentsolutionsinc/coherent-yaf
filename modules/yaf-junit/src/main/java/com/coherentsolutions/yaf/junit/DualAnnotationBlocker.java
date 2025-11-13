package com.coherentsolutions.yaf.junit;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

public class DualAnnotationBlocker implements ExecutionCondition {
    private static final ConditionEvaluationResult OK = ConditionEvaluationResult.enabled("ok");
    private static final ConditionEvaluationResult SKIP =
            ConditionEvaluationResult.disabled("replaced by template");

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext ctx) {
        var m = ctx.getTestMethod().orElse(null);
        if (m == null) return OK;
        boolean isTest = m.isAnnotationPresent(org.junit.jupiter.api.Test.class);
        boolean isTemplate = m.isAnnotationPresent(org.junit.jupiter.api.TestTemplate.class);
        boolean isParam = m.isAnnotationPresent(org.junit.jupiter.params.ParameterizedTest.class);
        return (isTest && isTemplate && !isParam) ? SKIP : OK;
    }
}

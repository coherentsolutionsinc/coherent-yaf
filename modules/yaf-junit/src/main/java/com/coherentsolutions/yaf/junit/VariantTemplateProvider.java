package com.coherentsolutions.yaf.junit;

import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

public class VariantTemplateProvider implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext ctx) {
        return ctx.getTestMethod().map(m ->
                AnnotationSupport.isAnnotated(m, org.junit.jupiter.api.TestTemplate.class) &&
                        // was originally @Test, we don’t touch real parameterized tests
                        AnnotationSupport.isAnnotated(m, org.junit.jupiter.api.Test.class) &&
                        !AnnotationSupport.isAnnotated(m, org.junit.jupiter.params.ParameterizedTest.class)
        ).orElse(false);
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext ctx) {
        Method method = ctx.getRequiredTestMethod();
        return Variants.ALL.stream().map(v -> new Ctx(method, v));
    }

    static class Ctx implements TestTemplateInvocationContext {
        private final Method method;
        private final String variant;

        Ctx(Method method, String variant) {
            this.method = method;
            this.variant = variant;
        }

        @Override
        public String getDisplayName(int idx) {
            return method.getName() + "[" + variant + "]";
        }

        @Override
        public List<Extension> getAdditionalExtensions() {
            return List.of(
                    (BeforeEachCallback) ec -> Variants.CURRENT.set(variant),
                    (AfterEachCallback) ec -> Variants.CURRENT.remove(),
                    new InvocationInterceptor() {
                        @Override
                        public void interceptTestTemplateMethod(Invocation<Void> invocation,
                                                                ReflectiveInvocationContext<Method> ignored,
                                                                ExtensionContext ec) throws Throwable {
                            Object testInstance = ec.getRequiredTestInstance();
                            // respect parameter resolvers if your @Test has params
                            ec.getExecutableInvoker().invoke(method, testInstance);
                            // JUnit 5.11+: declare we handled it
                            invocation.skip();
                        }
                    }
            );
        }
    }
}
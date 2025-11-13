package com.coherentsolutions.yaf.junit;

import org.junit.jupiter.api.extension.ExecutableInvoker;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.reflect.Method;

public class VariantSingleMethodInterceptor implements InvocationInterceptor {

    @Override
    public void interceptTestMethod(Invocation<Void> invocation,
                                    ReflectiveInvocationContext<Method> ctx,
                                    ExtensionContext ec) throws Throwable {
        Method m = ctx.getExecutable();

        // Leave parameterized tests alone
        if (AnnotationSupport.isAnnotated(m, org.junit.jupiter.params.ParameterizedTest.class)) {
            invocation.proceed();
            return;
        }

        // We’ll run it ourselves for each variant:
        invocation.skip(); // ✅ critical: don’t call proceed() in a loop

        Object testInstance = ec.getRequiredTestInstance();
        // Prefer ExecutableInvoker to preserve parameter resolvers:
        ExecutableInvoker invoker = ec.getExecutableInvoker();

        for (String v : Variants.ALL) {
            Variants.CURRENT.set(v);
            try {
                invoker.invoke(m, testInstance); // resolves params if any
            } catch (Throwable t) {
                // annotate which variant failed
                AssertionError ae = new AssertionError("Variant '" + v + "' failed", t);
                throw ae;
            } finally {
                Variants.CURRENT.remove();
            }
        }
    }

}

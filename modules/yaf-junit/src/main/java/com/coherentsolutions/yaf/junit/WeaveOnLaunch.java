package com.coherentsolutions.yaf.junit;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.method.MethodList;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.jar.asm.AnnotationVisitor;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;
import net.bytebuddy.pool.TypePool;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherSessionListener;

import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;


public class WeaveOnLaunch implements LauncherSessionListener {

    @Override
    public void launcherSessionOpened(LauncherSession session) {
        // Dynamically attach Byte Buddy (no external jar needed)
        var inst = ByteBuddyAgent.install(); // ⚠️ Needs -XX:+EnableDynamicAgentLoading to hide the warning

        var testTemplateAnn = AnnotationDescription.Builder
                .ofType(org.junit.jupiter.api.TestTemplate.class)
                .build();

        new AgentBuilder.Default()
                .type(nameStartsWith("com.yourcompany.tests"))
                .transform((builder, type, cl, module, protectionDomain) ->
                        builder.visit(new AsmVisitorWrapper.AbstractBase() {
                            @Override
                            public ClassVisitor wrap(TypeDescription instrumentedType, ClassVisitor classVisitor, Implementation.Context implementationContext, TypePool typePool, FieldList<FieldDescription.InDefinedShape> fields, MethodList<?> methods, int writerFlags, int readerFlags) {
                                return new ClassVisitor(Opcodes.ASM9, classVisitor) {
                                    @Override
                                    public MethodVisitor visitMethod(int acc, String name, String desc, String sig, String[] ex) {
                                        MethodVisitor mv = super.visitMethod(acc, name, desc, sig, ex);
                                        return new MethodVisitor(Opcodes.ASM9, mv) {
                                            @Override
                                            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                                                if ("Lorg/junit/jupiter/api/Test;".equals(descriptor)) {
                                                    // drop @Test
                                                    return null;
                                                }
                                                return super.visitAnnotation(descriptor, visible);
                                            }
                                        };
                                    }
                                };
                            }
                        })
                )
                .installOn(inst);
    }
}

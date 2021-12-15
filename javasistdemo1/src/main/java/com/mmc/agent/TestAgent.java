package com.mmc.agent;

import javassist.*;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

public class TestAgent {

    public static void premain(String agentArgs, Instrumentation inst) throws NotFoundException, UnmodifiableClassException, ClassNotFoundException, CannotCompileException, IOException {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                if(!"com/mmc/agent/UserService".equals(className)){
                    return null;
                }else {
                    byte[] bytes= new byte[0];
                    try {
                        bytes = buildMonitorClass();
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return bytes;
                }
            }
        },true);



    }


    public static byte[] buildMonitorClass() throws NotFoundException, CannotCompileException, IOException, IOException {
        /**
         * 1. 拷贝一个新方法
         * 2. 修改原方法名称
         * 3. 加监听代码
         */

        ClassPool classPool=new ClassPool();
        ClassPath classPath = classPool.appendSystemPath();
        CtClass ctClass = classPool.get("com.mmc.agent.UserService");
        CtMethod ctMethod = ctClass.getDeclaredMethod("sayHello");
        CtMethod newMethod = CtNewMethod.copy(ctMethod, ctClass, new ClassMap());
        ctMethod.setName("sayHello$agent");
        newMethod.setBody("{ long start=System.nanoTime();\n" +
                "        try{\n" +
                "            sayHello$agent();\n" +
                "        }finally {\n" +
                "            System.out.println(\"执行耗时：\"+(System.nanoTime()-start)+\"纳秒\");\n" +
                 "        }}");
        ctClass.addMethod(newMethod);
        return ctClass.toBytecode();
    }
}
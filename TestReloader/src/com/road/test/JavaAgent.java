package com.road.test;

import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import org.road.reloader.JavaDynAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.road.testfinal.BallMgr;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

public class JavaAgent
{
    public static final Logger logger = LoggerFactory.getLogger(JavaAgent.class);

    private VirtualMachine vm;

    private void init()
            throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException
    {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        
        System.err.println("thread id " + pid);
        // 虚拟机加载
        vm = VirtualMachine.attach(pid);
        vm.loadAgent("agentreloader.jar");

        Instrumentation instrumentation = JavaDynAgent.getInstrumentation();
        if (instrumentation == null)
            System.err.println("instrumentation is null");
        else
            System.err.println("instrumentation ");
    }

    private void destroy() throws IOException
    {
        if (vm != null)
            vm.detach();
    }

    /**
     * 重新加载类
     *
     * @param classArr
     * @throws Exception
     */
    public void javaAgent(String root, String[] classArr) throws ClassNotFoundException, IOException,
            UnmodifiableClassException, AttachNotSupportedException, AgentLoadException, AgentInitializationException
    {
        // 1.整理需要重定义的类
        List<ClassDefinition> classDefList = new ArrayList<ClassDefinition>();
        for (String className : classArr)
        {
            Class<?> c = Class.forName(className);
            String classPath = "./bin/" + className.replace(".", "/")
                    + ".class";
            logger.error("class redefined:" + classPath);
            byte[] bytesFromFile = FileUtils.file2byte(classPath);
            ClassDefinition classDefinition = new ClassDefinition(c, bytesFromFile);
            classDefList.add(classDefinition);
        }
        // 2.redefine
        JavaDynAgent.getInstrumentation().redefineClasses(
                classDefList.toArray(new ClassDefinition[classDefList.size()]));
    }

    public static void main(String[] args)
    {
        JavaAgent agent = new JavaAgent();

        BallMgr ballMgr = new BallMgr();

        try
        {
            agent.init();

            while (true)
            {
                agent.javaAgent(null, new String[]
                { "com.road.testfinal.BallMgr" });

                System.err.println(ballMgr.add());

                Thread.sleep(1000);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                agent.destroy();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}

package com.ecode.utils;

import com.ecode.constant.MessageConstant;
import com.ecode.exception.DebugCodeException;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.ExecStartCmd;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.StreamType;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.*;
@Slf4j
public class RunCodeUtil {

    // 编程语言配置类
    public static class CodeDockerOption {
        public String env;           // Docker镜像
        public String compileCmd;    // 编译命令
        public String runCmd;        // 运行命令
        public String fileSuffix;    // 源代码文件后缀
        public boolean needsCompilation; // 是否需要编译

        public CodeDockerOption(String env, String compileCmd, String runCmd, String fileSuffix, boolean needsCompilation) {
            this.env = env;
            this.compileCmd = compileCmd;
            this.runCmd = runCmd;
            this.fileSuffix = fileSuffix;
            this.needsCompilation = needsCompilation;
        }
    }

    // 语言配置映射
    private static final Map<String, CodeDockerOption> imageMap = Map.of(
            "cpp", new CodeDockerOption("run-cpp:1.0", "g++ /home/user/Main.cpp -o Main.out", "/home/user/Main.out", "cpp", true),
            "java", new CodeDockerOption("run-java:1.0", "javac /home/user/Main.java", "java -cp /home/user Main", "java", true),
            "python3", new CodeDockerOption("run-python3:1.0", null, "python3 /home/user/Main.py", "py", false)
    );

    public static String runCode(String url, int timeout,String codeType, String codeStr, String input) {
        CodeDockerOption codeDockerOption = imageMap.get(codeType);
        if (codeDockerOption == null){
            throw new DebugCodeException(MessageConstant.NO_LANGUAGE);
        }

        // 转义代码
        codeStr = codeStr.replace("\\","\\\\\\").replace("\"", "\\\"").replace("$", "\\$");
        String createFileCmd = String.format("echo \"%s\" > %s", codeStr, "/home/user/Main." + codeDockerOption.fileSuffix);


        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(url) // 或者 tcp://127.0.0.1:2375
                .build();

        // 使用 httpclient5 创建 DockerHttpClient
        DockerHttpClient dockerHttpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(3000)
                .build();

        // 创建 DockerClient 实例
        DockerClient dockerClient = DockerClientBuilder.getInstance(config)
                .withDockerHttpClient(dockerHttpClient)
                .build();

        // 连接到 Docker 守护进程
//            DockerClient dockerClient = DockerClientBuilder.getInstance(url).build();

            // 创建一个带有 OpenJDK 的容器
            CreateContainerResponse container = dockerClient.createContainerCmd(codeDockerOption.env)
                    .withHostConfig(HostConfig.newHostConfig()
                            .withMemory(512 * 1024 * 1024L) // 限制内存为512MB
                            .withMemorySwap(512 * 1024 * 1024L) // 限制内存+交换区总共512MB
                            .withCpuQuota(70000L))  // 限制每 100ms 使用 70ms 的 CPU)
                    .withCmd("tail", "-f", "/dev/null")  // 保持容器运行
                    .exec();

            // 启动容器
            dockerClient.startContainerCmd(container.getId()).exec();

            String result;
            try {
                // 执行创建文件命令
                execInContainer(dockerClient, container.getId(), createFileCmd,null);

                // 编译 Java 文件
                if (codeDockerOption.needsCompilation){
                    result = execWithTimeout(timeout,() -> execInContainer(dockerClient, container.getId(), codeDockerOption.compileCmd,null));
                    //log.debug("========编译输出: {}", result);
                }

                // 运行 Java 文件
                result = execWithTimeout(timeout,() -> execInContainer(dockerClient, container.getId(), codeDockerOption.runCmd,input));
                //log.debug("========运行输出: {}", result);
            }catch (Exception e){
                throw new DebugCodeException(e.getMessage());
            }finally {
                // 在后台线程中执行停止和移除容器的操作
                CompletableFuture.runAsync(() -> {
                    try {
                        // 停止容器
                        dockerClient.stopContainerCmd(container.getId()).exec();
                        // 移除容器
                        dockerClient.removeContainerCmd(container.getId()).exec();
                        log.info("容器{}\\n停止与移除成功", container.getId());
                    } catch (Exception e) {
                        e.printStackTrace();  // 捕获并打印异常
                    }
                });
            }
            return result;
    }

    /**
     * 容器内执行命令方法
     *
     * @param dockerClient 容器客户端
     * @param containerId  容器id
     * @param command      要执行的命令
     * @throws Exception 异常
     */
    private static String execInContainer(DockerClient dockerClient, String containerId, String command, String input) throws InterruptedException {
        String[] cmd = {"/bin/sh", "-c", command};
        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                .withCmd(cmd)
                .withAttachStdin(true)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .exec();

        // 使用 ByteArrayOutputStream 来捕获输出
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        // 将输入传递给容器
        InputStream inputStream = null;
        if (input != null) {
            inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        }

        ExecStartCmd execStartCmd = dockerClient.execStartCmd(execCreateCmdResponse.getId());
        log.info("input={}", input);
        if (inputStream != null) {
            execStartCmd.withStdIn(inputStream);  // 传入输入流
        }

        // 创建自定义的回调类，用于实时输出日志
        ExecStartResultCallback callback = new ExecStartResultCallback(outputStream, errorStream) {
            @Override
            public void onNext(Frame frame) {
                // 实时打印输出
                if (frame != null) {
                    String frameOutput = new String(frame.getPayload(), StandardCharsets.UTF_8);
                    if (frame.getStreamType() == StreamType.STDOUT) {
                        log.info("标准输出: {}\n======", frameOutput);  // 实时标准输出
                    } else if (frame.getStreamType() == StreamType.STDERR) {
                        log.error("STDERR: {}\n======", frameOutput);  // 实时错误输出
                    }
                }
                super.onNext(frame);  // 保持原有的处理逻辑
            }
        };

        execStartCmd.exec(callback).awaitCompletion();

        // 合并输出和错误日志并返回
        String stdout = outputStream.toString(StandardCharsets.UTF_8);
        String stderr = errorStream.toString(StandardCharsets.UTF_8);

        // 如果有错误输出，抛出异常
        if (!stderr.isEmpty()) {
            throw new DebugCodeException(stderr);
        }

        // 返回日志
        return stdout;
    }





    /**
     * 使用超时机制执行任务，超时后强制停止
     *
     * @param task 执行的任务
     * @return 任务结果
     */
    private static String execWithTimeout(int timeoutSeconds,Callable<String> task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(task);

        try {
            // 设定超时时间
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException e) {
            future.cancel(true);
            throw new DebugCodeException("任务执行超时");
        } catch (ExecutionException e) {
            // 解包 ExecutionException，抛出原始异常
            Throwable cause = e.getCause();
            if (cause instanceof DebugCodeException) {
                throw (DebugCodeException) cause;  // 重新抛出原始的 DebugCodeException
            } else {
                throw new DebugCodeException("任务运行异常:" + e.getMessage());
            }
        }finally {
            executor.shutdownNow();
        }
    }

}

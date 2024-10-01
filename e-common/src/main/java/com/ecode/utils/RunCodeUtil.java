package com.ecode.utils;

import com.ecode.exception.RunCodeException;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
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
import java.util.concurrent.*;
@Slf4j
public class RunCodeUtil {

    public static String runCode(String url, int timeout, String codeStr, String input) {
        // 转义代码
        codeStr = codeStr.replace("\\","\\\\\\").replace("\"", "\\\"").replace("$", "\\$");
        String createFileCmd = String.format("echo \"%s\" > %s", codeStr, "/root/Main.java");


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
            CreateContainerResponse container = dockerClient.createContainerCmd("openjdk:11")
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
                result = execWithTimeout(timeout,() -> execInContainer(dockerClient, container.getId(), "javac /root/Main.java",null));
                log.debug("========编译输出: {}", result);

                // 运行 Java 文件
                result = execWithTimeout(timeout,() -> execInContainer(dockerClient, container.getId(), "java -cp /root Main",null));
                log.debug("========运行输出: {}", result);
            }catch (Exception e){
                throw new RunCodeException(e.getMessage());
            }finally {
                // 停止并移除容器
                dockerClient.stopContainerCmd(container.getId()).exec();
                dockerClient.removeContainerCmd(container.getId()).exec();
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
        String execId = dockerClient.execCreateCmd(containerId)
                .withCmd(cmd)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withAttachStdin(input != null)  // 允许传入 stdin
                .exec()
                .getId();

        // 使用 ByteArrayOutputStream 来捕获输出
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        // 将输入传递给容器
        InputStream inputStream = null;
        if (input != null) {
            inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        }

        ExecStartCmd execStartCmd = dockerClient.execStartCmd(execId);
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
                        log.info("STDOUT: {}", frameOutput);  // 实时标准输出
                    } else if (frame.getStreamType() == StreamType.STDERR) {
                        log.error("STDERR: {}", frameOutput);  // 实时错误输出
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
            throw new RunCodeException(stderr);
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
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            future.cancel(true);
            throw new RunCodeException("任务执行超时");
        } finally {
            executor.shutdownNow();
        }
    }

}

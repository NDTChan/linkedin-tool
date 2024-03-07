package com.hanstack.linkedintool.config;

import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class SFTPConfig {

    private static String stRemoteHost;
    private static String stUsername;
    private static String stPassword;
    private static int stPort;

    private static SSHClient setupSshj() throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(new PromiscuousVerifier());
        client.connect("ftp.hanstack.vn", 22);
        client.useCompression();
        client.authPassword("hanstack", "Admin@10525597");
        return client;
    }

    public static void main(String[] args) {
        File file = new File("/Users/teahan/Desktop/www.linkedin.com_28-02-2024.json");
        uploadFile(file);
    }

    public static void uploadFile(File file) {
        try (SSHClient sshClient = setupSshj()) {
            try (SFTPClient sftpClient = sshClient.newSFTPClient()) {
                String dest = file.getName() + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "_file.json";
                sftpClient.put(new FileSystemFile(file), "public_ftp/" + dest);
                log.info("Upload file {} success", dest);
            }
        } catch (Exception ex) {
            log.error("Error when uploading file: {}", ex.getMessage());
        }
    }

    public static File downloadFile(String source) throws IOException {
        FileSystemFile fileSystemFile = new FileSystemFile(File.createTempFile("temp", ".tmp"));
        try (SSHClient sshClient = setupSshj()) {
            try (SFTPClient sftpClient = sshClient.newSFTPClient()) {
                sftpClient.get(source, fileSystemFile);
                log.info("Download file {} success", source);
            }
        } catch (Exception ex) {
            log.error("Error when downloading file", ex.getCause());
        }
        return fileSystemFile.getFile();
    }

    @Value("${sftp.host:ftp.hanstack.vn}")
    public void setRemoteHost(String remoteHost) {
        this.stRemoteHost = remoteHost;
    }

    @Value("${sftp.username:hanstack}")
    public void setUsername(String username) {
        this.stUsername = username;
    }

    @Value("${sftp.password:Admin@10525597}")
    public void setPassword(String password) {
        this.stPassword = password;
    }

    @Value("${sftp.port:21}")
    public void setPort(int port) {
        this.stPort = port;
    }
}

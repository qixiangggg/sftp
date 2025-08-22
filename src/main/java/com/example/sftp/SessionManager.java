package com.example.sftp;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SessionManager {
    private String host;
    private int port;
    private String username;
    private String password;

    public SessionManager(String type, int port, String username, String password) {
        this.host = type;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public Session createSession() throws JSchException {
        JSch jsch = new JSch();
        Session session =  jsch.getSession(username,host,port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking","no");
        return session;
    }
    public Session getSession() throws JSchException{
        Session session = createSession();
        return session;
    }
}

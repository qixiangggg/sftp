package com.example.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class ChannelSftpConnectionsFactory extends BasePooledObjectFactory<ChannelSftp> {
    private SessionManager sessionManager;

    public ChannelSftpConnectionsFactory(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public ChannelSftp create() throws Exception {
        ChannelSftp channelSftp = (ChannelSftp) sessionManager.getSession().openChannel("sftp");
        channelSftp.connect();

        return channelSftp;
    }

    @Override
    public PooledObject<ChannelSftp> wrap(ChannelSftp channelSftp) {
        return new DefaultPooledObject<>(channelSftp);
    }

    @Override
    public void destroyObject(PooledObject<ChannelSftp> pooledObject) throws Exception {
        ChannelSftp sftp = pooledObject.getObject();
        disconnectChannel(sftp);
    }

    private void disconnectChannel(ChannelSftp sftp) {
        if(sftp.isConnected()){
            sftp.disconnect();
        }
    }

    @Override
    public void passivateObject(PooledObject<ChannelSftp> pooledObject) throws Exception {
        ChannelSftp sftp = pooledObject.getObject();
        try{
            sftp.cd(sftp.getHome());
        }catch (SftpException ex){
            disconnectChannel(sftp);
        }
    }

    @Override
    public boolean validateObject(PooledObject<ChannelSftp> p) {
        ChannelSftp sftp = p.getObject();
        return sftp.isConnected() && !sftp.isClosed();
    }
}

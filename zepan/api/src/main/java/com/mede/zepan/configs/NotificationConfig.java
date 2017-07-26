package com.mede.zepan.configs;

import com.mede.zepan.Zepan;

/**
 * Created by JWang on 5/25/2017.
 */
public class NotificationConfig {
    
    public static final String ALL = "ALL";
    public static final String SUCCESS = "SUCCESS";
    
    private String path;
    private String user;
    private String password;
    private String type;
    private String host;
    private Integer port;
    private String mailFrom;
    private String mailtoHost;
    private Zepan.Protocol protocol;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String mailtoHost) {
        this.host = mailtoHost;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMailtoHost() {
        return mailtoHost;
    }

    public void setMailtoHost(String mailtoHost) {
        this.mailtoHost = mailtoHost;
    }

    public Zepan.Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Zepan.Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotificationConfig that = (NotificationConfig) o;

        if (path != null ? !path.equals(that.path) : that.path != null) {
            return false;
        }
        if (user != null ? !user.equals(that.user) : that.user != null) {
            return false;
        }
        if (password != null ? !password.equals(that.password) : that.password != null) {
            return false;
        }
        if (type != null ? !type.equals(that.type) : that.type != null) {
            return false;
        }
        if (host != null ? !host.equals(that.host) : that.host != null) {
            return false;
        }
        if (port != null ? !port.equals(that.port) : that.port != null) {
            return false;
        }
        if (mailFrom != null ? !mailFrom.equals(that.mailFrom) : that.mailFrom != null) {
            return false;
        }
        if (mailtoHost != null ? !mailtoHost.equals(that.mailtoHost) : that.mailtoHost != null) {
            return false;
        }
        return protocol != null ? protocol.equals(that.protocol) : that.protocol == null;

    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (mailFrom != null ? mailFrom.hashCode() : 0);
        result = 31 * result + (mailtoHost != null ? mailtoHost.hashCode() : 0);
        result = 31 * result + (protocol != null ? protocol.hashCode() : 0);
        return result;
    }
}

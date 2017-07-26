package com.mede.zepan.utils;

import com.mede.zepan.Notifier;
import com.mede.zepan.NotifierFactory;
import com.mede.zepan.configs.NotificationConfig;
import com.mede.zepan.exceptions.NotifyException;

/**
 * Created by JWang on 6/26/2017.
 */
public class LogNotifier implements Notifier {


    protected NotificationConfig config;

    public LogNotifier(NotificationConfig config) {
        this.config = config;
    }

    @Override
    public void notify(String type, String processInfo) throws NotifyException {
        log.info("Zepan ProcessInfo " + type + ":\n" + processInfo);
    }


    public static class LogNotifierFactory implements NotifierFactory {

        @Override
        public Notifier newNotifier(NotificationConfig config) {
            return new LogNotifier(config);
        }
    }
}

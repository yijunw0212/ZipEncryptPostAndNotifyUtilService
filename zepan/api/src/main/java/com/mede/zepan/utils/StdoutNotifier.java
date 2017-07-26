package com.mede.zepan.utils;

import com.mede.zepan.Notifier;
import com.mede.zepan.NotifierFactory;
import com.mede.zepan.configs.NotificationConfig;
import com.mede.zepan.exceptions.NotifyException;

/**
 *
 */
public class StdoutNotifier implements Notifier {

    protected NotificationConfig config;

    public StdoutNotifier(NotificationConfig config) {
        this.config = config;
    }

    @Override
    public void notify(String type, String processInfo) throws NotifyException {
        System.out.println("Received notification of type " + type);
        System.out.println(processInfo);
    }
    
    public static class StdoutNotifierFactory implements NotifierFactory {

        @Override
        public Notifier newNotifier(NotificationConfig config) {
            return new StdoutNotifier(config);
        }
    }
}

package com.mede.zepan;

import com.mede.zepan.configs.NotificationConfig;

/**
 *
 */
public interface NotifierFactory {

    
    Notifier newNotifier(NotificationConfig config);
}

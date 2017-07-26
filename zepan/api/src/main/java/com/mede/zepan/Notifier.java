package com.mede.zepan;

import com.mede.zepan.exceptions.NotifyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public interface Notifier {

    Logger log = LoggerFactory.getLogger(Notifier.class);
    
    void notify(String type, String processInfo) throws NotifyException;
}

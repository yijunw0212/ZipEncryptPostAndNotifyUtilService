package com.mede.zepan.test;

import java.io.IOException;

import com.mede.zepan.Notifier;
import com.mede.zepan.NotifierFactory;
import com.mede.zepan.configs.NotificationConfig;
import com.mede.zepan.exceptions.NotifyException;
import com.mede.zepan.ProcessInfo;
import com.mede.zepan.SuccessProcessInfo;
import com.mede.zepan.utils.ZepanUtils;

import static org.testng.Assert.assertEquals;

/**
 *
 */
public class TestNotifier implements Notifier {
    
    private Object objectToCompare;

    public TestNotifier(Object objectToCompare) {
        this.objectToCompare = objectToCompare;
    }

    @Override
    public void notify(String type, String processInfo) throws NotifyException {
        System.out.println(processInfo);
        Object received = null;
        try {
            if(type.equals(NotificationConfig.ALL))  {
                received = ZepanUtils.getAllInfoFromJson(processInfo);
                if(objectToCompare instanceof ProcessInfo) {
                    assertEquals(objectToCompare, received);
                }
            } else if (type.equals(NotificationConfig.SUCCESS)) {
                received = ZepanUtils.getSuccessInfoFromJson(processInfo);
                if (objectToCompare instanceof SuccessProcessInfo) {
                    assertEquals(objectToCompare, received);
                }
            } else {
                throw new IOException("Unexpected type:" + type);
            }
            
        } catch (IOException e) {
            throw new NotifyException(e);
        }

    }
    
    public static class TestNotifierFactory implements NotifierFactory {
        
        private Object testObject;

        public TestNotifierFactory(Object testObject) {
            this.testObject = testObject;
        }

        @Override
        public Notifier newNotifier(NotificationConfig config) {
            return new TestNotifier(testObject);
        }
    }
}

package com.mede.zepan.test;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import com.mede.zepan.utils.ZepanUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 *
 */
public class TestGrouping {
    
    @Test
    public void test4() throws Exception {
        URL url = Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("grouping/file1.txt");
        File f = new File(url.toURI());
        File grouping = f.getParentFile();
        File[] files = grouping.listFiles((File file) -> !file.getName()
                                                       .startsWith(".") && !file.isDirectory());
        Collection<Collection<File>> groups = ZepanUtils.groupFiles(4, files);
        assertEquals(groups.size(), 3);
        Iterator<Collection<File>> it = groups.iterator();
        assertEquals(it.next().size(), 4);
        assertEquals(it.next().size(), 4);
        assertEquals(it.next().size(), 2);
    }

    @Test
    public void test5() throws Exception {
        URL url = Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("grouping/file1.txt");
        File f = new File(url.toURI());
        File grouping = f.getParentFile();
        File[] files = grouping.listFiles((File file) -> !file.getName()
                                                              .startsWith(".") && !file.isDirectory());
        Collection<Collection<File>> groups = ZepanUtils.groupFiles(5, files);
        assertEquals(groups.size(), 2);
        Iterator<Collection<File>> it = groups.iterator();
        assertEquals(it.next()
                       .size(), 5);
        assertEquals(it.next()
                       .size(), 5);
    }

}

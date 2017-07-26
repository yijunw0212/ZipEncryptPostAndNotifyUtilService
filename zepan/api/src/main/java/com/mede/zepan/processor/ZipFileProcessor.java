package com.mede.zepan.processor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Checksum;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.mede.zepan.IOProcessor;
import com.mede.zepan.exceptions.ZepanException;
import com.mede.zepan.FileInfo;
import com.mede.zepan.utils.ZepanUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 */
public class ZipFileProcessor extends IOProcessor {
    
    private ZipOutputStream outputStream;
    private long max;
    private boolean closed = false;
    private CountingChecksum counter = new CountingChecksum();
    private FileInfo fileInfo;
    private StringBuilder inputs = new StringBuilder();

    public ZipFileProcessor(File output, long max) {
        super(output);
        this.max = max;
    }
    
    @Override
    public void process(FileInfo info, InputStream inputStream)
        throws ZepanException {
        info.setOutputPath(output.getAbsolutePath());
        if (this.fileInfo == null) {
            this.fileInfo = info.copy();
            this.fileInfo.setZipped(true);
        }
        try {
            if (outputStream == null) {
                OutputStream base = createOutputStream();
                this.outputStream = new ZipOutputStream(new CheckedOutputStream(base, counter));
            }
            ZipEntry e = new ZipEntry(ZepanUtils.getFileName(info.getInputPath()));
            if(inputs.length() == 0) {
                inputs.append(info.getInputPath());
            } else {
                inputs.append(",").append(info.getInputPath());
            }
            if (log.isDebugEnabled()) {
                log.debug("ZipFileProcessor.process created new zip entry :" + e.getName());
            }
            outputStream.putNextEntry(e);
            inputStream = createInputStream(inputStream);
            IOUtils.copyLarge(inputStream, outputStream);
            outputStream.closeEntry();
            inputStream.close();
            
            if (log.isDebugEnabled()) {
                log.debug("ZipFileProcessor.process current file info state:" + fileInfo);
            }
            if (log.isDebugEnabled()) {
                log.debug("ZipFileProcessor.process: counter size:" + counter.getValue() + " max: " + max);
            }
            if(counter.getValue() >= max) {
                close();
            }
             
        } catch (IOException e1) {
            try {
                close();
            } catch (IOException e) {
                log.warn("Failed to close stream", e);
            }
            throw new ZepanException(e1);
        }
    }

    @Override
    public FileInfo getFileInfo() {
        return fileInfo;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        }
        fileInfo.setInputPath(inputs.toString());
        if(outputStream != null) {
            outputStream.flush();
            outputStream.close();
        }
        closed = true;
    }

    /**
     * Used to count the bytes written to zip, which will be less
     * than the bytes read from uncompressed input.
     */
    private class CountingChecksum implements Checksum {
        
        private long count = 0;

        @Override
        public void update(int b) {
           count++; 
        }

        @Override
        public void update(byte[] b, int off, int len) {
            count += (len - off);
        }

        @Override
        public long getValue() {
            return count;
        }

        @Override
        public void reset() {
            count = 0;
        }
    }
    
}

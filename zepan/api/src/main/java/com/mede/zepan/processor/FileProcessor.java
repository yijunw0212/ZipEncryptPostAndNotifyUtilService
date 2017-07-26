package com.mede.zepan.processor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mede.zepan.IOProcessor;
import com.mede.zepan.exceptions.ZepanException;
import com.mede.zepan.FileInfo;
import org.apache.commons.io.IOUtils;

/**
 *
 */
public class FileProcessor extends IOProcessor {
    
    private FileInfo fileInfo;
    
    public FileProcessor(File output) {
        super(output);
    }

    @Override
    public void process(FileInfo info, InputStream inputStream)
        throws ZepanException {
        info.setOutputPath(output.getAbsolutePath());
        this.fileInfo = info.copy();
        OutputStream outputStream = null;
        try {
            outputStream = createOutputStream();
            inputStream = createInputStream(inputStream);
            IOUtils.copyLarge(inputStream, outputStream);
            inputStream.close();
            close();
        } catch (IOException e1) {
            throw new ZepanException(e1);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    log.warn("Error closing streams", e);
                }
            }
        }
    }

    @Override
    public FileInfo getFileInfo() {
        return fileInfo;
    }

    @Override
    public boolean isClosed() {
        return true;
    }

    @Override
    public void close() throws IOException {
        
    }
}

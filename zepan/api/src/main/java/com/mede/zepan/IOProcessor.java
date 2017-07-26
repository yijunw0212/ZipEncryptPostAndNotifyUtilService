package com.mede.zepan;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.mede.zepan.exceptions.ZepanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Process input and write to output
 */
public abstract class IOProcessor implements Closeable {
    
    protected static Logger log = LoggerFactory.getLogger(com.mede.zepan.IOProcessor.class);
    
    protected File output;
    private List<InputStreamDecorator> inputStreamDecorators = new ArrayList<>();
    private List<OutputStreamDecorator> outputStreamDecorators = new ArrayList<>();

    public IOProcessor(File output) {
        this.output = output;
    }
    
    public void accept(InputStreamDecorator inputStreamDecorator) {
        this.inputStreamDecorators.add(inputStreamDecorator);
    }
    
    public void accept(OutputStreamDecorator outputStreamDecorator) {
        this.outputStreamDecorators.add(outputStreamDecorator);
    }
    
    protected OutputStream createOutputStream() throws IOException, ZepanException {
        OutputStream out = new FileOutputStream(output);
        for (OutputStreamDecorator outputStreamDecorator : outputStreamDecorators) {
            out = outputStreamDecorator.decorate(out);
        }
        return out;
    }

    protected InputStream createInputStream(InputStream in) throws IOException, ZepanException {
        for (InputStreamDecorator inputStreamDecorator : inputStreamDecorators) {
            in = inputStreamDecorator.decorate(in);
        }
        return in;
    }

    public abstract void process(FileInfo info, InputStream inputStream) throws ZepanException;
    
    public abstract FileInfo getFileInfo();
    
    public abstract boolean isClosed();
    
    
    
}

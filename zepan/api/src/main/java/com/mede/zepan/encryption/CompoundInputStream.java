package com.mede.zepan.encryption;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A stream that takes multiple underlying streams and reads from them consecutively.
 */
public class CompoundInputStream extends InputStream {
    /**
     * The sequence of input streams that will be returned.
     */
    private final InputStream[] streams;

    private int curr;

    private int total;

    /**
     * The current input stream.
     */
    private InputStream currStream;

    /**
     * Creates a compound stream from a previously populated byte[] and the not-exhausted
     * stream from which the bytes were read.
     *
     * @param previouslyRead
     * @param stream
     */
    public CompoundInputStream(final byte[] previouslyRead, final InputStream stream) {
        this(new ByteArrayInputStream(previouslyRead), stream);
    }

    /**
     * create CompoundInputStream with an array of input streams and boolean
     *
     * @param streams an array of streams
     */
    public CompoundInputStream(final InputStream... streams) {
        if (streams == null || streams.length < 1) {
            throw new IllegalArgumentException(
                "input stream array must not be null and must have a length of at least 1.");
        }
        this.streams = streams;
        curr = -1;
        this.total = streams.length;
        moveToNextStream();
    }

    /**
     * this returns what is currently available in the current stream. There is no way of knowing
     * (without reading in
     * all streams), whether this constitutes the whole stream, and hence, whether the available
     * total should include
     * the available from the following stream.
     *
     * @return
     * @throws IOException
     */
    public int available() throws IOException {
        return (currStream.available());
    }

    /**
     * closes all streams
     *
     * @throws IOException
     */
    public void close() throws IOException {
        for (int i = 0; i < total; i++) {
            streams[i].close();
        }
    }

    /**
     * returns false
     *
     * @return
     */
    public boolean markSupported() {
        return false;
    }

    /**
     * if isContinuousRead() is true, then the streams are read from continuously. If not, then the
     * read method returns
     * -1 at the end of each stream, and a call to next() will produce a move to the next stream in
     * the array. hasNext()
     * returns true, while there are more streams to read from <strong>unless</strong>
     * isContinuousRead() returns true,
     * in which case hasNext() will return false.
     */
    public int read() throws IOException {
        int ret = currStream.read();
        if (ret == -1 && moveToNextStream()) {
            return read();
        }
        return ret;
    }

    /**
     * this returns the amount of bytes read in the current stream if not in continous mode. If in
     * continuous mode, and
     * the length of requested data is greater than the current stream can supply, then the next
     * stream is moved to, if
     * there are any left.
     *
     * @param b
     * @param off
     * @param len
     * @return
     * @throws IOException
     */
    public int read(final byte[] b, final int off, final int len) throws IOException {
        return read1(b, off, len);
    }

    public int read(final byte[] b) throws IOException {
        return read1(b, 0, b.length);
    }

    private int read1(final byte[] b, final int off, final int len) throws IOException {
        int ret = currStream.read(b, off, len);
        if (ret == -1) {
            if (moveToNextStream()) {
                return read1(b, off, len);
            } else {
                return ret;
            }
        }
        while (ret < len) {
            if (moveToNextStream()) {
                int next = read1(b, off + ret, len - ret);
                if (next != -1) {
                    ret += next;
                }
            } else {
                break;
            }
        }
        return ret;
    }

    public long skip(final long n) throws IOException {
        long s = currStream.skip(n);
        if (s < n && moveToNextStream()) {
            s += skip(n - s);
        }
        return s;
    }

    private boolean moveToNextStream() {
        if (hasNext()) {
            currStream = streams[++curr];
            return true;
        }
        return false;
    }

    /**
     * returns true more streams are available.
     *
     * @return
     */
    private boolean hasNext() {
        return curr < (total - 1);
    }
}


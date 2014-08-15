package com.edgar.core.shiro;

import org.apache.commons.io.input.TeeInputStream;

import javax.servlet.ServletInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

class ServletInputStreamImpl extends ServletInputStream {

        private InputStream is;
        ByteArrayOutputStream writerStream = new ByteArrayOutputStream();

        public ServletInputStreamImpl(InputStream is) {
            this.is = is;
        }

        public int read() throws IOException {
            new TeeInputStream(is, writerStream);
            return is.read();
        }

        public boolean markSupported() {
            return false;
        }

        public synchronized void mark(int i) {
            throw new RuntimeException(new IOException("mark/reset not supported"));
        }

        public synchronized void reset() throws IOException {
            throw new IOException("mark/reset not supported");
        }
    }
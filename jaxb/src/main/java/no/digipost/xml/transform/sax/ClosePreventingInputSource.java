/*
 * Copyright (C) Posten Norge AS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.digipost.xml.transform.sax;

import org.xml.sax.InputSource;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;


class ClosePreventingInputSource extends InputSource {

    private boolean initialized;

    public ClosePreventingInputSource(InputStream inputStream, Charset encoding) {
        super(new ClosePreventingInputStream(inputStream));
        if (encoding != null) {
            this.setEncoding(encoding.name());
        }
        this.initialized = true;
    }

    private static final class ClosePreventingInputStream extends FilterInputStream {
        public ClosePreventingInputStream(InputStream in) {
            super(in);
        }
        @Override
        public void close() {
            // prevent closing
        }
    }



    @Override
    public final Reader getCharacterStream() {
        return null;
    }

    @Override
    public final void setCharacterStream(Reader characterStream) {
        throw new UnsupportedOperationException("Setting a character stream is not supported");
    }

    @Override
    public final void setByteStream(InputStream byteStream) {
        if (initialized) {
            throw new UnsupportedOperationException("Setting a byte stream is not supported");
        }
        super.setByteStream(byteStream);
    }
}

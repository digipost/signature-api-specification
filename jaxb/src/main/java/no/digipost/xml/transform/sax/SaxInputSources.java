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

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import static java.util.Objects.requireNonNull;

public final class SaxInputSources {

    /**
     * Create a SAX {@link InputSource} wrapping the given existing InputStream
     * which will not be closed by anything consuming the InputSource.
     * The wrapped InputStream, which will be returned by {@link InputSource#getByteStream()},
     * will be ensured cannot be closed, even if attempted. The responsibility to
     * properly manage an existing InputStream (i.e. closing)
     * is outside the InputSource, and it cannot assume that the wrapped stream
     * can be closed after done consuming it.
     */
    public static InputSource fromInputStreamPreventClose(InputStream inputStream) {
        return fromInputStreamPreventClose(inputStream, null);
    }

    /**
     * Create a SAX {@link InputSource} wrapping the given existing InputStream
     * which will not be closed by anything consuming the InputSource.
     *
     * @see #fromInputStreamPreventClose(InputStream)
     */
    public static InputSource fromInputStreamPreventClose(InputStream inputStream, Charset encoding) {
        return new ClosePreventingInputSource(inputStream, encoding);
    }


    public static InputSource fromClasspath(String resourceName) {
        return fromClasspath(resourceName, null);
    }

    public static InputSource fromClasspath(String resourceName, Charset encoding) {
        return fromClasspath(resourceName, encoding, UrlInputSource.class.getClassLoader());
    }

    public static InputSource fromClasspath(String resourceName, Charset encoding, ClassLoader classLoader) {
        URL location = classLoader.getResource(resourceName.startsWith("/") ? resourceName.substring(1) : resourceName);
        requireNonNull(location, resourceName + " not found on classpath");
        return new UrlInputSource(location, encoding);
    }

    private SaxInputSources() {
    }
}

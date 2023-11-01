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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.Charset;

import static java.util.Objects.requireNonNull;

public class UrlInputSource extends InputSource {

    public static UrlInputSource fromClasspath(String resourceName, Charset encoding) {
        return fromClasspath(resourceName, encoding, UrlInputSource.class.getClassLoader());
    }

    public static UrlInputSource fromClasspath(String resourceName, Charset encoding, ClassLoader classLoader) {
        URL location = classLoader.getResource(resourceName.startsWith("/") ? resourceName.substring(1) : resourceName);
        requireNonNull(location, resourceName + " not found on classpath");
        return new UrlInputSource(location, encoding);
    }

    private URL location;

    public UrlInputSource(URL location, Charset encoding) {
        this.location = location;
        this.setEncoding(encoding.name());
        this.setSystemId(location.toString());
    }

    @Override
    public final InputStream getByteStream() {
        try {
            return location.openStream();
        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Unable to obtain InputStream for " + location +
                    " because " + e.getClass().getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public final String getEncoding() {
        return super.getEncoding();
    }

    @Override
    public final Reader getCharacterStream() {
        return null;
    }

}

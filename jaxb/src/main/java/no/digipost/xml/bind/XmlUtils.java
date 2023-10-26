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
package no.digipost.xml.bind;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Collection;

import static java.util.Objects.requireNonNull;

final class XmlUtils {

    public static Schema createSchema(Collection<String> schemaResourceNames) {
        if (schemaResourceNames == null || schemaResourceNames.isEmpty()) {
            throw new IllegalArgumentException("No sources given for creating Schema. (schemaResourceNames was " + schemaResourceNames + ")");
        }
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
            parserFactory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);

            SAXParser saxParser = parserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            Source[] schemaSources = schemaResourceNames.stream()
                .map(resource -> new SAXSource(xmlReader, createInputSource(resource)))
                .toArray(Source[]::new);

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(schemaSources);
            return schema;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not create schema from resources [" + String.join(", ", schemaResourceNames) + "] " +
                    "because " + e.getClass().getSimpleName() + ": " + e.getMessage(), e);
        }
    }


    public static InputSource createInputSource(String resourceName) {
        URL resourceUrl = requireNonNull(XmlUtils.class.getResource(resourceName), resourceName);
        try (InputStream inputStream = resourceUrl.openStream()) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            final int bufLen = 1024;
            byte[] buf = new byte[bufLen];
            int readLen;
            while ((readLen = inputStream.read(buf, 0, bufLen)) != -1)
                outputStream.write(buf, 0, readLen);

            InputSource source = new InputSource(new ByteArrayInputStream(outputStream.toByteArray()));
            source.setSystemId(resourceUrl.toString());
            return source;
        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Unable to resolve " + resourceName + " from " + resourceUrl + ", " +
                    "because " + e.getClass().getSimpleName() + " '" + e.getMessage() + "'", e);
        }
    }


    private XmlUtils() {
    }
}

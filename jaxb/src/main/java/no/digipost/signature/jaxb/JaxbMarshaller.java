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
package no.digipost.signature.jaxb;

import no.digipost.signature.xsd.SignatureApiSchemas;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
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
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

/**
 * @see JaxbMarshaller.ForResponsesOfAllApis
 * @see JaxbMarshaller.ForRequestsOfAllApis
 */
public class JaxbMarshaller {

    /**
     * Marshaller for creating requests for both the Direct and Portal API.
     * This marshaller will validate if the XML generated from marshalled Java objects
     * adheres to the API schemas.
     */
     public static class ForRequestsOfAllApis extends JaxbMarshaller {

        public static JaxbMarshaller singleton() {
            return SingletonHolder.instance;
        }

        private static final class SingletonHolder {
            private static final JaxbMarshaller instance =  new JaxbMarshaller.ForRequestsOfAllApis();
        }

        public ForRequestsOfAllApis() {
            super(SignatureMarshalling.allApiRequestClasses(), SignatureApiSchemas.DIRECT_AND_PORTAL_API);
        }
    }


    /**
     * Marshaller (unmarshaller) for reading responses from both the Direct and Portal API.
     * This marshaller does <em>not</em> validate the XML before attempting to unmarshal it to Java objects.
     */
    public static class ForResponsesOfAllApis extends JaxbMarshaller {

        public static JaxbMarshaller singleton() {
            return SingletonHolder.instance;
        }

        private static final class SingletonHolder {
            private static final JaxbMarshaller instance = new JaxbMarshaller.ForResponsesOfAllApis();
        }

        public ForResponsesOfAllApis() {
            super(SignatureMarshalling.allApiResponseClasses());
        }
    }



    /**
     * Marshaller for both the Direct and Portal API.
     * <p><strong>
     *  This marshaller is intended only to be used internally by Posten signering, as both
     *  requests and responses are validated against schemas. A 3rd party client should <em>not</em>
     *  validate responses from the API.
     * </strong>
     *
     * @see JaxbMarshaller.ForResponsesOfAllApis
     * @see JaxbMarshaller.ForRequestsOfAllApis
     */
    public static class ForAllApis extends JaxbMarshaller {

        public static JaxbMarshaller singleton() {
            return SingletonHolder.instance;
        }

        private static final class SingletonHolder {
            private static final JaxbMarshaller instance = new JaxbMarshaller.ForAllApis();
        }

        public ForAllApis() {
            super(SignatureMarshalling.allApiClasses(), SignatureApiSchemas.DIRECT_AND_PORTAL_API);
        }
    }

    private static InputSource createInputSource(String resource) {
        URL resourceUrl = requireNonNull(JaxbMarshaller.class.getResource(resource), resource);
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
                    "Unable to resolve " + resource + " from " + resourceUrl + ", " +
                    "because " + e.getClass().getSimpleName() + " '" + e.getMessage() + "'", e);
        }
    }

    private static Schema createSchema(Collection<String> resources) {
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);
            parserFactory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);

            SAXParser saxParser = parserFactory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            Source[] schemaSources = resources.stream()
                .map(resource -> new SAXSource(xmlReader, createInputSource(resource)))
                .toArray(Source[]::new);

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(schemaSources);
            return schema;
        } catch (Exception e) {
            throw new RuntimeException("Could not create schema from resources [" + String.join(", ", resources) + "]", e);
        }
    }

    private static JAXBContext initContext(Collection<Class<?>> classes) {
        Class<?>[] classesToBeBound = classes.toArray(new Class[classes.size()]);
        try {
            return JAXBContext.newInstance(classesToBeBound);
        } catch (JAXBException e) {
            throw new RuntimeException("Could not create JAXBContext for classes [" + Stream.of(classesToBeBound).map(Class::getSimpleName).collect(joining(",")) + "]" , e);
        }
    }

    private final JAXBContext jaxbContext;
    private final Optional<Schema> schema;

    public JaxbMarshaller(Set<Class<?>> classes, Set<String> schemaResources) {
        this.jaxbContext = initContext(classes);
        this.schema = Optional.ofNullable(schemaResources).filter(s -> !s.isEmpty()).map(JaxbMarshaller::createSchema);
    }

    public JaxbMarshaller(Set<Class<?>> classes) {
        this(classes, null);
    }

    public String marshalToString(Object object) {
        return marshalToResult(object, xml -> xml.toString(UTF_8.name()));
    }

    public byte[] marshalToBytes(Object object) {
        return marshalToResult(object, ByteArrayOutputStream::toByteArray);
    }

    @FunctionalInterface
    private interface ThrowingFunction<T, R> {
        R apply(T t) throws Exception;
    }

    private <R> R marshalToResult(Object object, ThrowingFunction<? super ByteArrayOutputStream, ? extends R> outputStreamMapper) {
        try (ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream(128)) {
            marshal(object, xmlOutputStream);
            return outputStreamMapper.apply(xmlOutputStream);
        } catch (SignatureMarshalException marshalException) {
            throw marshalException;
        } catch (Exception e) {
            throw SignatureMarshalException.failedMarshal(object, e);
        }
    }


    public void marshal(Object object, OutputStream outputStream) {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            schema.ifPresent(marshaller::setSchema);
            marshaller.marshal(object, outputStream);
        } catch (Exception e) {
            throw SignatureMarshalException.failedMarshal(object, e);
        }
    }

    public <T> T unmarshal(InputStream inputStream, Class<T> type) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            schema.ifPresent(unmarshaller::setSchema);
            return type.cast(unmarshaller.unmarshal(inputStream));
        } catch (Exception e) {
            throw SignatureMarshalException.failedUnmarshal(type, e);
        }
    }

    public <T> T unmarshal(byte[] bytes, Class<T> type) {
        return unmarshal(new ByteArrayInputStream(bytes), type);
    }

}

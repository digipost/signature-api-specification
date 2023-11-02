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
package no.digipost.xml.validation;

import no.digipost.xml.parsers.SaxParserProvider;
import no.digipost.xml.transform.sax.SaxInputSources;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import java.util.Collection;

public final class SchemaHelper {

    public static Schema createW3cXmlSchema(Collection<String> schemaResourceNames) {
        if (schemaResourceNames == null || schemaResourceNames.isEmpty()) {
            throw new IllegalArgumentException("No sources given for creating Schema. (schemaResourceNames was " + schemaResourceNames + ")");
        }
        try {
            Source[] schemaSources = schemaResourceNames.stream()
                    .map(SaxInputSources::fromClasspath)
                    .map(SaxParserProvider.createSecuredProvider()::createSource)
                    .toArray(Source[]::new);

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            return schemaFactory.newSchema(schemaSources);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not create schema from resources [" + String.join(", ", schemaResourceNames) + "] " +
                    "because " + e.getClass().getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    private SchemaHelper() {
    }
}

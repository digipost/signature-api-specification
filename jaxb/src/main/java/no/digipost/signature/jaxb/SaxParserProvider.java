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

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING;

class SaxParserProvider {

    public static SaxParserProvider createSecuredParserFactory() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);

        // configuration to align with OWASP recommendations:
        // https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html#jaxb-unmarshaller
        try {
            factory.setFeature(FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setValidating(false);  // this only concerns DTD validation
            factory.setXIncludeAware(false);  // already false by default, but setting anyway
        } catch (SAXNotRecognizedException | SAXNotSupportedException | ParserConfigurationException e) {
            throw new IllegalStateException(
                    "Unable to configure " + factory.getClass().getName() + " for secure processing " +
                    "because " + e.getClass().getSimpleName() + ": " + e.getMessage(), e);
        }

        return new SaxParserProvider(factory);
    }


    /**
     * A configured SAXParserFactory's {@link SAXParserFactory#newSAXParser() newSAXParser()} method is
     * <a href="https://jcp.org/aboutJava/communityprocess/review/jsr063/jaxp-pd2.pdf">expected by specification to be thread safe  (ch. 3.4)</a>
     */
    private final SAXParserFactory saxParserFactory;

    public SaxParserProvider(SAXParserFactory saxParserFactory) {
        this.saxParserFactory = saxParserFactory;
    }


    public SAXParser createParser() {
        try {
            return saxParserFactory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new IllegalStateException(
                    "Unable to create new SAX parser from " + saxParserFactory.getClass().getName() +
                    " because " + e.getClass().getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    public XMLReader createXMLReader() {
        SAXParser parser = createParser();
        try {
            return parser.getXMLReader();
        } catch (SAXException e) {
            throw new IllegalStateException(
                    "Unable to get " + XMLReader.class.getName() + " from the created " + parser.getClass().getName() +
                    " because " + e.getClass().getSimpleName() + ": " + e.getMessage(), e);
        }
    }
}

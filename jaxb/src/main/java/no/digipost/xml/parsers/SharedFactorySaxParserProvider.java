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
package no.digipost.xml.parsers;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

class SharedFactorySaxParserProvider implements SaxParserProvider {

    /**
     * A configured SAXParserFactory's {@link SAXParserFactory#newSAXParser() newSAXParser()} method is
     * <a href="https://jcp.org/aboutJava/communityprocess/review/jsr063/jaxp-pd2.pdf">expected by specification to be thread safe  (ch. 3.4)</a>
     */
    private final SAXParserFactory saxParserFactory;

    SharedFactorySaxParserProvider(SAXParserFactory saxParserFactory) {
        this.saxParserFactory = saxParserFactory;
    }


    @Override
    public SAXParser createParser() {
        try {
            return saxParserFactory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new IllegalStateException(
                    "Unable to create new SAX parser from " + saxParserFactory.getClass().getName() +
                    " because " + e.getClass().getSimpleName() + ": " + e.getMessage(), e);
        }
    }
}

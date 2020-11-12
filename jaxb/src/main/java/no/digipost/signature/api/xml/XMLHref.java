/**
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
package no.digipost.signature.api.xml;

import no.digipost.signature.api.xml.thirdparty.xmldsig.Reference;
import no.digipost.signature.jaxb.adapter.HrefXmlAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.zip.ZipEntry;

@XmlJavaTypeAdapter(HrefXmlAdapter.class)
public final class XMLHref {

    public static XMLHref referenceTo(Reference xmlDSigSignatureReference) {
        return of(xmlDSigSignatureReference.getURI());
    }

    public static XMLHref referenceTo(ZipEntry zipEntry) {
        return of(zipEntry.getName());
    }

    public static XMLHref of(String href) {
        try {
            return new XMLHref(URLDecoder.decode(href, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    private final String href;

    private XMLHref(String href) {
        this.href = href;
    }

    public String asUrlEncodedString() {
        try {
            return URLEncoder.encode(href, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public String asString() {
        return href;
    }

    @Override
    public String toString() {
        return "href='" + href + "'";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof XMLHref) {
            XMLHref that = (XMLHref) other;
            return Objects.equals(this.href, that.href);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(href);
    }

}

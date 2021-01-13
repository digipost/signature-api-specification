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

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static co.unruly.matchers.Java8Matchers.where;
import static co.unruly.matchers.OptionalMatchers.contains;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class XMLPortalSignatureJobTest {

    @Test
    void viewLegacySingularDocumentManifestAsMultipleDocumentsManifest() {
        XMLLegacyPortalDocument document = new XMLLegacyPortalDocument("title", "non-sensitive title", "description", XMLHref.of("href"), "application/pdf");
        XMLPortalSignatureJobManifest manifest = new XMLPortalSignatureJobManifest().withDocument(document);
        assertThat(manifest, where(XMLManifest::getDocumentsToSign, Matchers.contains(document)));
        assertThat(manifest.getJobInformation(), where(JobInformation::getTitle, both(is("title")).and(is(manifest.getDocument().getTitle()))));
        assertThat(manifest.getJobInformation(), where(JobInformation::getNonSensitiveTitle, contains("non-sensitive title")));
        assertThat(manifest.getJobInformation(), where(JobInformation::getDescription, both(contains("description")).and(contains(manifest.getDocument().getDescription()))));
    }

    @Test
    void multipleDocumentsManifest() {
        XMLPortalDocument document = new XMLPortalDocument("document title", XMLHref.of("href"), "application/pdf");
        XMLPortalSignatureJobManifest manifest = new XMLPortalSignatureJobManifest()
                .withTitle("title").withNonsensitiveTitle("non-sensitive title").withDescription("description")
                .withDocuments(document);
        assertThat(manifest, where(XMLManifest::getDocumentsToSign, Matchers.contains(document)));
        assertThat(manifest.getJobInformation(), where(JobInformation::getTitle, is("title")));
        assertThat(manifest.getJobInformation(), where(JobInformation::getNonSensitiveTitle, contains("non-sensitive title")));
        assertThat(manifest.getJobInformation(), where(JobInformation::getDescription, contains("description")));
        assertThat(manifest, where(XMLPortalSignatureJobManifest::getDocument, nullValue()));
    }


}

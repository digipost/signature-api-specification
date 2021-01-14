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

import java.util.List;

import static java.util.Arrays.asList;

interface MultipleDocumentsManifestWithLegacySingleDocumentSupport extends XMLManifest {

    @Override
    @SuppressWarnings("deprecation")
    default JobInformation getJobInformation() {
        no.digipost.signature.api.xml.legacy.XMLLegacyDocument singleDocument = getDocument();
        if (singleDocument != null) {
            return new JobInformation(singleDocument.getTitle(), singleDocument.getNonsensitiveTitle(), singleDocument.getDescription());
        } else {
            return new JobInformation(getTitle(), getNonsensitiveTitle(), getDescription());
        }
    }

    @Override
    default List<? extends XMLDocument> getDocumentsToSign() {
        @SuppressWarnings("deprecation")
        XMLDocument singleDocument = getDocument();
        return singleDocument != null ? asList(singleDocument) : getDocuments();
    }

    List<? extends XMLDocument> getDocuments();

    String getTitle();

    String getDescription();

    String getNonsensitiveTitle();

}

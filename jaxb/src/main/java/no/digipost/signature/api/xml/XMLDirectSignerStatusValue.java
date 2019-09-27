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

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class XMLDirectSignerStatusValue {

    private static final ConcurrentMap<String, XMLDirectSignerStatusValue> KNOWN = new ConcurrentHashMap<>();

    /**
     * The signer has not yet signed the document.
     */
    public static final XMLDirectSignerStatusValue WAITING = XMLDirectSignerStatusValue.of("WAITING");

    /**
     * The user decided to reject to sign the document, and has been redirected to the
     * {@link XMLExitUrls#getRejectionUrl() rejection-url} provided in the
     * {@link XMLDirectSignatureJobRequest#getExitUrls() XMLDirectSignatureJobRequest's exit-urls}.
     */
    public static final XMLDirectSignerStatusValue REJECTED = XMLDirectSignerStatusValue.of("REJECTED");

    /**
     * The user didn't sign the document before the job expired.
     */
    public static final XMLDirectSignerStatusValue EXPIRED = XMLDirectSignerStatusValue.of("EXPIRED");

    /**
     * An unexpected error occured when signing, and the user has been
     * redirected to the {@link XMLExitUrls#getErrorUrl() error-url} provided in the
     * {@link XMLDirectSignatureJobRequest#getExitUrls() XMLDirectSignatureJobRequest's exit-urls}.
     */
    public static final XMLDirectSignerStatusValue FAILED = XMLDirectSignerStatusValue.of("FAILED");

    /**
     * The document has been signed, and the signer has been redirected to the
     * {@link XMLExitUrls#getCompletionUrl() completion-url} provided in the
     * {@link XMLDirectSignatureJobRequest#getExitUrls() XMLDirectSignatureJobRequest's exit-urls}.
     * The signed document artifacts can be downloaded by following the appropriate urls in the
     * {@link XMLDirectSignatureJobStatusResponse}.
     */
    public static final XMLDirectSignerStatusValue SIGNED = XMLDirectSignerStatusValue.of("SIGNED");

    /**
     * Indicates that the service was unable to retrieve the signer's name. This might happen because
     * the lookup service is unavailable at the time of signing, but the name can also be unavailable
     * permanently. Senders may choose to try re-creating this signature job.
     * <p>
     * Only applicable for authenticated signatures where the sender requires signed documents to
     * contain name as the signer's identifier.
     */
    public static final XMLDirectSignerStatusValue SIGNERS_NAME_NOT_AVAILABLE = XMLDirectSignerStatusValue.of("SIGNERS_NAME_NOT_AVAILABLE");

    /**
     * The job has reached a state where the status of this signature is not applicable.
     * This includes (but is not limited to) the case where a signer rejects to sign, and thus
     * ending the job in a failed state. Any remaining (previously {@link XMLDirectSignerStatusValue#WAITING WAITING})
     * signatures are marked as {@code NOT_APPLICABLE}.
     */
    public static final XMLDirectSignerStatusValue NOT_APPLICABLE = XMLDirectSignerStatusValue.of("NOT_APPLICABLE");


    public static XMLDirectSignerStatusValue of(String xmlValue) {
        Objects.requireNonNull(xmlValue, "String value for " + XMLDirectSignerStatusValue.class.getSimpleName());
        return KNOWN.computeIfAbsent(xmlValue.trim().toUpperCase(), key -> new XMLDirectSignerStatusValue(xmlValue.trim()));
    }


    private final String xmlValue;

    private XMLDirectSignerStatusValue(String xmlValue) {
        this.xmlValue = xmlValue;
    }

    public String asString() {
        return xmlValue;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof XMLDirectSignerStatusValue
                && Objects.equals(this.xmlValue, ((XMLDirectSignerStatusValue) other).xmlValue);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(xmlValue);
    }

    @Override
    public String toString() {
        return XMLDirectSignerStatusValue.class.getSimpleName() + " '" + xmlValue + "'";
    }

}

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

public final class XMLPortalSignatureStatusValue {

    private static final ConcurrentMap<String, XMLPortalSignatureStatusValue> KNOWN = new ConcurrentHashMap<>();

    /**
     * The signer has not yet been resolved.
     * Either waiting for the job to be made available or for the signer to perform the signature.
     */
    public static final XMLPortalSignatureStatusValue WAITING = XMLPortalSignatureStatusValue.of("WAITING");

    /**
     * The signer rejected the job.
     */
    public static final XMLPortalSignatureStatusValue REJECTED = XMLPortalSignatureStatusValue.of("REJECTED");

    /**
     * The job owner cancelled the job.
     */
    public static final XMLPortalSignatureStatusValue CANCELLED = XMLPortalSignatureStatusValue.of("CANCELLED");

    /**
     * Indicates that the signer is reserved against digital communication from the government.
     * This is a status returned from
     * <a href="https://samarbeid.difi.no/felleslosninger/kontakt-og-reservasjonsregisteret">Difi Kontakt og reservasjonsregisteret</a>,
     * and is only relevant for government senders.
     */
    public static final XMLPortalSignatureStatusValue RESERVED = XMLPortalSignatureStatusValue.of("RESERVED");

    /**
     * Indicates that no contact information could be found for the signer. This will happen when no contact information is provided
     * when creating the signature job, and no contact information could be found in
     * <a href="https://samarbeid.difi.no/felleslosninger/kontakt-og-reservasjonsregisteret">Difi Kontakt og reservasjonsregisteret</a>
     * (when applicable).
     */
    public static final XMLPortalSignatureStatusValue CONTACT_INFORMATION_MISSING = XMLPortalSignatureStatusValue.of("CONTACT_INFORMATION_MISSING");

    /**
     * The user didn't sign the document before the job expired.
     */
    public static final XMLPortalSignatureStatusValue EXPIRED = XMLPortalSignatureStatusValue.of("EXPIRED");

    /**
     * The document has been signed, and the signer has been redirected to the
     * {@link XMLExitUrls#getCompletionUrl() completion-url} provided in the
     * {@link XMLDirectSignatureJobRequest#getExitUrls() XMLDirectSignatureJobRequest's exit-urls}.
     * The signed document artifacts can be downloaded by following the appropriate urls in the
     * {@link XMLDirectSignatureJobStatusResponse}.
     */
    public static final XMLPortalSignatureStatusValue SIGNED = XMLPortalSignatureStatusValue.of("SIGNED");

    /**
     * The signer entered the wrong security code too many times.
     * Only applicable for signers addressed by e-mail address or mobile number.
     */
    public static final XMLPortalSignatureStatusValue BLOCKED = XMLPortalSignatureStatusValue.of("BLOCKED");

    /**
     * Indicates that the service was unable to retrieve the signer's name. This happens when the
     * signer's name is permanently unavailable in the lookup service, creating and signing a
     * new signature job with the same signer will yield the same result.
     * <p>
     * Only applicable for authenticated signatures where the sender requires signed documents to
     *  contain name as the signer's identifier.
     */
    public static final XMLPortalSignatureStatusValue SIGNERS_NAME_NOT_AVAILABLE = XMLPortalSignatureStatusValue.of("SIGNERS_NAME_NOT_AVAILABLE");

    /**
     * The job has reached a state where the status of this signature is not applicable.
     * This includes (but is not limited to) the case where a signer rejects to sign, and thus
     * ending the job in a failed state. Any remaining (previously {@link XMLPortalSignatureStatusValue#WAITING WAITING})
     * signatures are marked as {@code NOT_APPLICABLE}.
     */
    public static final XMLPortalSignatureStatusValue NOT_APPLICABLE = XMLPortalSignatureStatusValue.of("NOT_APPLICABLE");


    public static XMLPortalSignatureStatusValue of(String xmlValue) {
        Objects.requireNonNull(xmlValue, "String value for " + XMLPortalSignatureStatusValue.class.getSimpleName());
        return KNOWN.computeIfAbsent(xmlValue.trim().toUpperCase(), key -> new XMLPortalSignatureStatusValue(xmlValue.trim()));
    }


    private final String xmlValue;

    private XMLPortalSignatureStatusValue(String xmlValue) {
        this.xmlValue = xmlValue;
    }

    public String asString() {
        return xmlValue;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof XMLPortalSignatureStatusValue
                && Objects.equals(this.xmlValue, ((XMLPortalSignatureStatusValue) other).xmlValue);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(xmlValue);
    }

    @Override
    public String toString() {
        return XMLPortalSignatureStatusValue.class.getSimpleName() + " '" + xmlValue + "'";
    }

}

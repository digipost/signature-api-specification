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
package no.digipost.signature.xsd;

import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

/**
 * If depending on
 * <a href="http://search.maven.org/#search%7Cga%7C1%7Cg%3Ano.digipost.signature%20a%3Asignature-api-specification">signature-api-specification</a>,
 * these {@code String} constants can be used to resolve the schemas from classpath.
 * <p>
 * The {@code String} constants contains the resource names for the individual schema files, but the sets
 * {@link #DIRECT_AND_PORTAL_API}, {@link #DIRECT_API}, and {@link #PORTAL_API} compiles all relevant schemas
 * for the possible API integration cases.
 */
public final class SignatureApiSchemas {

    public static final String DIRECT_AND_PORTAL_SCHEMA = "/direct-and-portal.xsd";
    public static final String DIRECT_ONLY_SCHEMA = "/direct.xsd";
    public static final String PORTAL_ONLY_SCHEMA = "/direct.xsd";

    public static final String XMLDSIG_SCHEMA = "/thirdparty/xmldsig-core-schema.xsd";
    public static final String XADES_SCHEMA = "/thirdparty/XAdES.xsd";
    public static final String ASICE_SCHEMA = "/thirdparty/ts_102918v010201.xsd";

    /**
     * All schemas for both the Direct- and Portal API
     */
    public static final Set<String> DIRECT_AND_PORTAL_API = unmodifiableSet(new LinkedHashSet<>(asList(DIRECT_AND_PORTAL_SCHEMA, XMLDSIG_SCHEMA, XADES_SCHEMA, ASICE_SCHEMA)));

    /**
     * The schemas for the Direct API
     */
    public static final Set<String> DIRECT_API = unmodifiableSet(new LinkedHashSet<>(asList(DIRECT_ONLY_SCHEMA, XMLDSIG_SCHEMA, XADES_SCHEMA, ASICE_SCHEMA)));

    /**
     * The schemas for the Portal API
     */
    public static final Set<String> PORTAL_API = unmodifiableSet(new LinkedHashSet<>(asList(DIRECT_ONLY_SCHEMA, XMLDSIG_SCHEMA, XADES_SCHEMA, ASICE_SCHEMA)));

}

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
package no.digipost.signature.jaxb.spring;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

final class SpringUtils {

    static Set<Resource> asClassPathResources(Iterable<String> schemaResourceNames) {
        Set<Resource> schemas = new LinkedHashSet<>();
        for (String schemaResourceName : schemaResourceNames) {
            schemas.add(new ClassPathResource(schemaResourceName));
        }
        return unmodifiableSet(schemas);
    }

    private SpringUtils() { }
}

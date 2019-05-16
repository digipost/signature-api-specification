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
package no.digipost.signature.xsd;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.lang.reflect.Modifier.isStatic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

public class SignatureApiSchemasTest {

    @Test
    public void findsAllSchemasOnClasspath() throws Exception {
        Class<SignatureApiSchemas> schemasClass = SignatureApiSchemas.class;
        Set<Field> singleSchemaFields = new LinkedHashSet<>();
        for (Field field : schemasClass.getFields()) {
            if (isStatic(field.getModifiers()) && field.getType() == String.class) {
                singleSchemaFields.add(field);
            }
        }
        assertThat(singleSchemaFields, hasSize(greaterThan(0)));
        for (Field singleSchema : singleSchemaFields) {
            String resourceName = (String) singleSchema.get(schemasClass);
            try (InputStream schema = schemasClass.getResourceAsStream(resourceName)) {
                assertThat(resourceName, schema, notNullValue());
            }
        }
    }
}

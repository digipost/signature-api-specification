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
package no.digipost.signature.api.xml;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static co.unruly.matchers.Java8Matchers.where;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static no.digipost.DiggExceptions.mayThrow;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertAll;

class XMLDirectSignerStatusValueTest {

    @Test
    void correctEqualsHashCode() {
        EqualsVerifier.forClass(XMLDirectSignerStatusValue.class).verify();
    }

    @Test
    void constructingFromStringValueOfConstantsYieldsSameInstance() {
        Stream<XMLDirectSignerStatusValue> knownStatuses = Stream.of(XMLDirectSignerStatusValue.class.getDeclaredFields())
            .filter(f -> f.getType() == XMLDirectSignerStatusValue.class)
            .filter(f -> isPublic(f.getModifiers()) && isStatic(f.getModifiers()))
            .map(mayThrow((Field constantField) -> (XMLDirectSignerStatusValue) constantField.get(XMLDirectSignerStatusValue.class)).asUnchecked());

        assertAll(knownStatuses.flatMap(status -> Stream.of(
                () -> assertThat(status.asString(), where(XMLDirectSignerStatusValue::of, sameInstance(status))),
                () -> assertThat(" " + status.asString().toLowerCase() + " ", where(XMLDirectSignerStatusValue::of, sameInstance(status)))
                )));
    }


}

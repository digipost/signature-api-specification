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
package no.digipost.xml.bind;

import org.junit.jupiter.api.Test;

import static no.digipost.xml.bind.CustomAssertions.assertCommutative;
import static no.digipost.xml.bind.MarshallingCustomization.NO_CUSTOMIZATION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;

public class MarshallingCustomizationTest {

    @Test
    void andThenUsedOn_NO_CUSTOMIZATION_AlwaysYieldsOtherOperand() {
        MarshallingCustomization myCustomization = MarshallingCustomization.of(marshaller -> {}, unmarshaller -> {});
        assertThat(assertCommutative(MarshallingCustomization::andThen, NO_CUSTOMIZATION, myCustomization), sameInstance(myCustomization));
    }

}

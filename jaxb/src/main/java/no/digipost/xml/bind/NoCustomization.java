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

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

final class NoCustomization implements MarshallerCustomizer, UnmarshallerCustomizer {

    private final String description;

    NoCustomization(String description) {
        this.description = description;
    }

    @Override
    public void customize(Unmarshaller marshaller) {
    }

    @Override
    public void customize(Marshaller marshaller) {
    }

    @Override
    public MarshallerCustomizer andThen(MarshallerCustomizer nextCustomization) {
        return nextCustomization;
    }

    @Override
    public UnmarshallerCustomizer andThen(UnmarshallerCustomizer nextCustomization) {
        return nextCustomization;
    }

    @Override
    public String toString() {
        return description;
    }

}

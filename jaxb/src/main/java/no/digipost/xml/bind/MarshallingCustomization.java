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
import javax.xml.validation.Schema;

import java.util.Set;

/**
 * Encapsulates customizing the {@link Marshaller} and {@link Unmarshaller} used for JAXB marshalling.
 *
 * @see MarshallerCustomizer
 * @see UnmarshallerCustomizer
 */
public final class MarshallingCustomization {

    public static MarshallingCustomization validateUsingSchemaResources(Set<String> schemaResources) {
        return of(
                MarshallerCustomizer.validateUsingSchemaResources(schemaResources),
                UnmarshallerCustomizer.validateUsingSchemaResources(schemaResources));
    }

    public static MarshallingCustomization validateUsingSchema(Schema schema) {
        return of(MarshallerCustomizer.validateUsingSchema(schema), UnmarshallerCustomizer.validateUsingSchema(schema));
    }

    public static MarshallingCustomization onUnmarshalling(UnmarshallerCustomizer customizer) {
        return of(MarshallerCustomizer.NO_CUSTOMIZATION, customizer);
    }

    public static MarshallingCustomization onMarshalling(MarshallerCustomizer customizer) {
        return of(customizer, UnmarshallerCustomizer.NO_CUSTOMIZATION);
    }

    public static MarshallingCustomization of(MarshallerCustomizer marshallerCustomizer, UnmarshallerCustomizer unmarshallerCustomizer) {
        if (marshallerCustomizer instanceof NoCustomization && unmarshallerCustomizer instanceof NoCustomization) {
            return NO_CUSTOMIZATION;
        }
        return new MarshallingCustomization(marshallerCustomizer, unmarshallerCustomizer);
    }

    public static final MarshallingCustomization NO_CUSTOMIZATION =
            new MarshallingCustomization(MarshallerCustomizer.NO_CUSTOMIZATION, UnmarshallerCustomizer.NO_CUSTOMIZATION, "MarshallingCustomization.NO_CUSTOMIZATION");



    private final MarshallerCustomizer marshallerCustomizer;
    private final UnmarshallerCustomizer unmarshallerCustomizer;
    private final String description;

    private MarshallingCustomization(MarshallerCustomizer marshallerCustomizer, UnmarshallerCustomizer unmarshallerCustomizer) {
        this(marshallerCustomizer, unmarshallerCustomizer, null);
    }

    private MarshallingCustomization(MarshallerCustomizer marshallerCustomizer, UnmarshallerCustomizer unmarshallerCustomizer, String description) {
        this.marshallerCustomizer = marshallerCustomizer;
        this.unmarshallerCustomizer = unmarshallerCustomizer;
        this.description = description;
    }

    public MarshallingCustomization andThen(MarshallingCustomization nextCustomization) {
        if (nextCustomization == NO_CUSTOMIZATION) {
            return this;
        } else if (this == NO_CUSTOMIZATION) {
            return nextCustomization;
        } else {
            return of(
                    this.marshallerCustomizer.andThen(nextCustomization.marshallerCustomizer),
                    this.unmarshallerCustomizer.andThen(nextCustomization.unmarshallerCustomizer));
        }
    }

    public MarshallingCustomization andThenOnUnmarshalling(UnmarshallerCustomizer additionalCustomizer) {
        return andThen(onUnmarshalling(this.unmarshallerCustomizer.andThen(additionalCustomizer)));
    }

    public MarshallingCustomization andThenOnMarshalling(MarshallerCustomizer additionalCustomizer) {
        return andThen(onMarshalling(this.marshallerCustomizer.andThen(additionalCustomizer)));
    }

    public void customize(Unmarshaller unmarshaller) throws Exception {
        unmarshallerCustomizer.customize(unmarshaller);
    }

    public void customize(Marshaller marshaller) throws Exception {
        marshallerCustomizer.customize(marshaller);
    }

    @Override
    public String toString() {
        return description != null ? description : super.toString();
    }

}

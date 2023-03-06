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
package no.digipost.jaxb;

import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBContextFactory;
import javax.xml.bind.JAXBException;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class JaxbContextFactoryResolveTest {

    private static final Class<?>[] jaxbClasses = { MyJaxbEntity.class };

    @Test
    void resolvesStaticJAXBContextFactory() throws JAXBException {
        JAXBContextFactory realFactory = StaticOldJaxb2ContextFactory.factory;
        JAXBContextFactory spiedFactory = spy(realFactory);
        try {
            StaticOldJaxb2ContextFactory.factory = spiedFactory;
            JAXBContext.newInstance(jaxbClasses);
        } finally {
            StaticOldJaxb2ContextFactory.factory = realFactory;
        }
        verify(spiedFactory).createContext(same(jaxbClasses), any());
    }

    @Test
    void overrideStaticJAXBContextFactoryUsingSystemProperty() throws JAXBException {
        Optional<String> existingFactoryPropertyValue = Optional.ofNullable(System.getProperty(JAXBContext.JAXB_CONTEXT_FACTORY));
        AtomicInteger invocationCounter = OverriddenJAXBContextFactory.countInteractionsIn(new AtomicInteger());
        try {
            System.setProperty(JAXBContext.JAXB_CONTEXT_FACTORY, "no.digipost.jaxb.OverriddenJAXBContextFactory");
            JAXBContext.newInstance(jaxbClasses);
        } finally {
            existingFactoryPropertyValue
                .map(value -> System.setProperty(JAXBContext.JAXB_CONTEXT_FACTORY, value))
                .orElseGet(() -> System.clearProperty(JAXBContext.JAXB_CONTEXT_FACTORY));
        }
        assertEquals(1, invocationCounter.get(), "invocations on overridden JAXBContext factory");
    }


    static class MyJaxbEntity {

    }

}

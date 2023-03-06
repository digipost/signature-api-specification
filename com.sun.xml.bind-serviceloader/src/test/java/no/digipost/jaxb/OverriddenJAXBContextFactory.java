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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class OverriddenJAXBContextFactory {

    private static final Collection<AtomicInteger> invocationCounters = new ConcurrentLinkedDeque<>();

    public static JAXBContext createContext(Class<?>[] classesToBeBound, Map<String, ?> properties) throws JAXBException {
        invocationCounters.forEach(AtomicInteger::incrementAndGet);
        return StaticOldJaxb2ContextFactory.createContext(classesToBeBound, properties);
    }

    public static AtomicInteger countInteractionsIn(AtomicInteger count) {
        invocationCounters.add(count);
        return count;
    }
}

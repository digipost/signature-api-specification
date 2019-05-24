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

import no.digipost.signature.jaxb.basics.LessFuzzToStringStrategy;
import org.junit.jupiter.api.Test;

import static co.unruly.matchers.Java8Matchers.where;
import static java.lang.Integer.toHexString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertAll;

class LessFuzzToStringStrategyTest {

    @Test
    void lessFuzzToStringSTrategyIsActivated() {

        XMLSms smsExample = new XMLSms("98219472");

        assertAll(
                "Should generate less noisy toStrings for generated JAXB classes with " +
                "<arg>-XtoString-toStringStrategyClass=" + LessFuzzToStringStrategy.class.getName() + "</arg> " +
                "in the configuration for jaxb2-maven-plugin in the pom.xml.",

                () -> assertThat("should not output fully qualified classname", smsExample, where(XMLSms::toString, startsWith(XMLSms.class.getSimpleName()))),
                () -> assertThat("should not include hashCode", smsExample, where(XMLSms::toString, not(containsString(toHexString(smsExample.hashCode())))))
            );

    }

}

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

import java.util.function.BinaryOperator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

final class CustomAssertions {

    public static <T> T assertCommutative(BinaryOperator<T> operation, T op1, T op2) {
        T result1 = operation.apply(op1, op2);
        T result2 = operation.apply(op2, op1);
        assertThat("order of arguments should not matter", result1, equalTo(result2));
        return result1;
    }

    private CustomAssertions() {}
}

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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

final class UrlEncoding {

    public static String decodeIfEncoded(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (IllegalArgumentException notUrlEncoded) {
            // illegal characters encountered while decoding, thus treating it as a
            // non-encoded string, as an encoded string would not contain illegal characters.
            return s;
        } catch (UnsupportedEncodingException utf8NotSupported) {
            throw new RuntimeException(utf8NotSupported.getMessage(), utf8NotSupported);
        }
    }

    public static String encode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException utf8NotSupported) {
            throw new RuntimeException(utf8NotSupported.getMessage(), utf8NotSupported);
        }
    }

}

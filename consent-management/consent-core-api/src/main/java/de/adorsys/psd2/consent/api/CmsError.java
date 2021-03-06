/*
 * Copyright 2018-2019 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.adorsys.psd2.consent.api;

import java.util.Optional;
import java.util.stream.Stream;

public enum CmsError {
    /**
     * Describes cases, when the error is caused by 3rd party libraries, network errors, etc.
     */
    TECHNICAL_ERROR,
    /**
     * Describes cases, when the error is caused by the mistakes in business logic (like providing wrong payment ID)
     */
    LOGICAL_ERROR,
    /**
     * Should be used in case of wrong AIS consent checksum after definite consent properties were set initially and then
     * are being changed.
     */
    CHECKSUM_ERROR;

    public static Optional<CmsError> getByName(String name) {
        return Stream.of(values())
                   .filter(v -> v.name().equalsIgnoreCase(name))
                   .findFirst();
    }

}

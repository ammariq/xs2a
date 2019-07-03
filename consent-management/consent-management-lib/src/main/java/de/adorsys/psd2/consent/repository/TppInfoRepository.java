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

package de.adorsys.psd2.consent.repository;

import de.adorsys.psd2.consent.domain.TppInfoEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TppInfoRepository extends Xs2aCrudRepository<TppInfoEntity, Long> {

    @Modifying
    @Query(value = "UPDATE tpp_info " +
               "SET cancelRedirectUri = :cancelRedirectUri, cancelNokRedirectUri = :cancelNokRedirectUri " +
               "WHERE id = :id")
    int updateCancelRedirectUrisById(@Param("id") Long id,
                                      @Param("cancelRedirectUri") String cancelRedirectUri,
                                      @Param("cancelNokRedirectUri") String cancelNokRedirectUri);
}

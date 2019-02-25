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

package de.adorsys.psd2.consent.service.aspsp;

import de.adorsys.psd2.consent.aspsp.api.piis.CmsAspspPiisFundsExportService;
import de.adorsys.psd2.consent.domain.piis.PiisConsentEntity;
import de.adorsys.psd2.consent.repository.PiisConsentRepository;
import de.adorsys.psd2.consent.repository.specification.PiisConsentEntitySpecification;
import de.adorsys.psd2.consent.service.mapper.PiisConsentMapper;
import de.adorsys.psd2.xs2a.core.piis.PiisConsent;
import de.adorsys.psd2.xs2a.core.psu.PsuIdData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CmsAspspPiisFundsExportServiceInternal implements CmsAspspPiisFundsExportService {
    private static final String DEFAULT_SERVICE_INSTANCE_ID = "UNDEFINED";

    private final PiisConsentRepository piisConsentRepository;
    private final PiisConsentEntitySpecification piisConsentEntitySpecification;
    private final PiisConsentMapper piisConsentMapper;

    @Override
    public Collection<PiisConsent> exportConsentsByTpp(String tppAuthorisationNumber,
                                                       @Nullable LocalDate createDateFrom,
                                                       @Nullable LocalDate createDateTo, @Nullable PsuIdData psuIdData,
                                                       @Nullable String instanceId) {
        if (StringUtils.isBlank(tppAuthorisationNumber)) {
            log.info("CmsAspspPiisFundsExportServiceInternal.exportConsentsByTpp failed, tppAuthorisationNumber is empty or null.");
            return Collections.emptyList();
        }

        String actualInstanceId = StringUtils.defaultIfEmpty(instanceId, DEFAULT_SERVICE_INSTANCE_ID);

        List<PiisConsentEntity> piisConsentEntities = piisConsentRepository.findAll(piisConsentEntitySpecification.byTppIdAndCreationPeriodAndPsuIdDataAndInstanceId(tppAuthorisationNumber, createDateFrom, createDateTo, psuIdData, actualInstanceId));
        return piisConsentMapper.mapToPiisConsentList(piisConsentEntities);
    }

    @Override
    public Collection<PiisConsent> exportConsentsByPsu(PsuIdData psuIdData, @Nullable LocalDate createDateFrom,
                                                       @Nullable LocalDate createDateTo, @Nullable String instanceId) {
        if (psuIdData == null || psuIdData.isEmpty()) {
            log.info("CmsAspspPiisFundsExportServiceInternal.exportConsentsByPsu failed, psuIdData is empty or null.");
            return Collections.emptyList();
        }

        String actualInstanceId = StringUtils.defaultIfEmpty(instanceId, DEFAULT_SERVICE_INSTANCE_ID);

        List<PiisConsentEntity> piisConsentEntities = piisConsentRepository.findAll(piisConsentEntitySpecification.byPsuIdDataAndCreationPeriodAndInstanceId(psuIdData, createDateFrom, createDateTo, actualInstanceId));
        return piisConsentMapper.mapToPiisConsentList(piisConsentEntities);
    }

    @Override
    public Collection<PiisConsent> exportConsentsByAccountId(@NotNull String aspspAccountId,
                                                             @Nullable LocalDate createDateFrom,
                                                             @Nullable LocalDate createDateTo,
                                                             @Nullable String instanceId) {
        if (StringUtils.isBlank(aspspAccountId)) {
            log.info("CmsAspspPiisFundsExportServiceInternal.exportConsentsByAccountId failed, aspspAccountId is empty or null.");
            return Collections.emptyList();
        }

        String actualInstanceId = StringUtils.defaultIfEmpty(instanceId, DEFAULT_SERVICE_INSTANCE_ID);

        List<PiisConsentEntity> piisConsentEntities = piisConsentRepository.findAll(piisConsentEntitySpecification.byAspspAccountIdAndCreationPeriodAndInstanceId(aspspAccountId, createDateFrom, createDateTo, actualInstanceId));
        return piisConsentMapper.mapToPiisConsentList(piisConsentEntities);
    }
}

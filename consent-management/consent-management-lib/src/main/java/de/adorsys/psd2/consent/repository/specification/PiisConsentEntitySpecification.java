/*
 * Copyright 2018-2020 adorsys GmbH & Co KG
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

package de.adorsys.psd2.consent.repository.specification;

import de.adorsys.psd2.consent.domain.AccountReferenceEntity;
import de.adorsys.psd2.consent.domain.account.AspspAccountAccess;
import de.adorsys.psd2.consent.domain.consent.ConsentEntity;
import de.adorsys.psd2.xs2a.core.consent.ConsentType;
import de.adorsys.psd2.xs2a.core.profile.AccountReference;
import de.adorsys.psd2.xs2a.core.profile.AccountReferenceSelector;
import de.adorsys.psd2.xs2a.core.psu.PsuIdData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import java.util.Currency;
import java.util.List;

import static de.adorsys.psd2.consent.repository.specification.EntityAttribute.*;
import static de.adorsys.psd2.consent.repository.specification.EntityAttributeSpecificationProvider.provideSpecificationForJoinedEntityAttribute;

@Service
public class PiisConsentEntitySpecification extends ConsentFilterableSpecification {
    private final CommonSpecification<ConsentEntity> commonSpecification;
    private final ConsentSpecification consentSpecification;

    public PiisConsentEntitySpecification(CommonSpecification<ConsentEntity> commonSpecification, ConsentSpecification consentSpecification) {
        super(commonSpecification, consentSpecification);
        this.commonSpecification = commonSpecification;
        this.consentSpecification = consentSpecification;
    }

    @Override
    public ConsentType getType() {
        return ConsentType.PIIS_ASPSP;
    }

    /**
     * Returns specification for some entity for filtering data by PSU data and instance id.
     *
     * @param psuIdData  PSU data
     * @param instanceId ID of particular service instance
     * @return resulting specification
     */
    public Specification<ConsentEntity> byPsuDataAndInstanceId(PsuIdData psuIdData, String instanceId) {
        return consentSpecification.byPsuDataInListAndInstanceId(psuIdData, instanceId)
                   .and(byConsentType());
    }

    /**
     * Returns specification for ConsentEntity for filtering consents by PsuIdData, TppInfo and AccountReference.
     *
     * @param psuIdData              mandatory PSU ID data
     * @param tppAuthorisationNumber mandatory TPP authorisation number
     * @param accountReference       mandatory PIIS Account Reference
     * @return resulting specification for ConsentEntity
     */
    public Specification<ConsentEntity> byPsuIdDataAndAuthorisationNumberAndAccountReference(@NotNull PsuIdData psuIdData,
                                                                                             @NotNull String tppAuthorisationNumber,
                                                                                             @NotNull AccountReference accountReference) {
        return Specification.where(commonSpecification.byPsuIdDataInList(psuIdData))
                   .and(consentSpecification.byTpp(tppAuthorisationNumber))
                   .and(byAccountReference(accountReference))
                   .and(byConsentType());
    }

    /**
     * Returns specification for ConsentEntity for filtering consents by Currency and Account Reference Selector.
     *
     * @param currency optional Currency
     * @param selector mandatory Account Reference Selector
     * @return resulting specification for ConsentEntity
     */
    public Specification<ConsentEntity> byCurrencyAndAccountReferenceSelector(@Nullable Currency currency, @NotNull AccountReferenceSelector selector) {
        return (root, query, cb) -> {
            Join<ConsentEntity, List<AspspAccountAccess>> aspspAccountAccessesJoin = root.join(ASPSP_ACCOUNT_ACCESSES_ATTRIBUTE);

            return Specification
                       .where(provideSpecificationForJoinedEntityAttribute(aspspAccountAccessesJoin, ACCOUNT_ACCESS_ATTRIBUTE_ACCOUNT_IDENTIFIER, selector.getAccountValue()))
                       .and(provideSpecificationForJoinedEntityAttribute(aspspAccountAccessesJoin, CURRENCY_ATTRIBUTE, currency))
                       .and(byConsentType())
                       .toPredicate(root, query, cb);
        };
    }

    /**
     * Returns specification for ConsentEntity for filtering consents by Account Reference Selector.
     *
     * @param selector mandatory Account Reference Selector
     * @return resulting specification for ConsentEntity
     */
    public Specification<ConsentEntity> byAccountReferenceSelector(@NotNull AccountReferenceSelector selector) {
        return (root, query, cb) -> {
            Join<ConsentEntity, AccountReferenceEntity> aspspAccountAccessesJoin = root.join(ASPSP_ACCOUNT_ACCESSES_ATTRIBUTE);

            return Specification
                       .where(provideSpecificationForJoinedEntityAttribute(aspspAccountAccessesJoin, ACCOUNT_ACCESS_ATTRIBUTE_ACCOUNT_IDENTIFIER, selector.getAccountValue()))
                       .and(byConsentType())
                       .toPredicate(root, query, cb);
        };
    }

    /**
     * Returns specification for ConsentEntity for filtering data by AccountReference.
     *
     * <p>
     * If optional parameter is not provided, this specification will not affect resulting data.
     *
     * @param accountReference PIIS Account Reference
     * @return resulting specification
     */
    private Specification<ConsentEntity> byAccountReference(@NotNull AccountReference accountReference) {
        return (root, query, cb) -> {
            AccountReferenceSelector selector = accountReference.getUsedAccountReferenceSelector();

            Join<ConsentEntity, List<AspspAccountAccess>> aspspAccountAccessJoin = root.join(ASPSP_ACCOUNT_ACCESSES_ATTRIBUTE);
            Specification<ConsentEntity> specifications = Specification
                                                              .where(provideSpecificationForJoinedEntityAttribute(aspspAccountAccessJoin, ACCOUNT_ACCESS_ATTRIBUTE_ACCOUNT_IDENTIFIER, selector.getAccountValue()));

            if (accountReference.getCurrency() != null) {
                specifications.and(provideSpecificationForJoinedEntityAttribute(aspspAccountAccessJoin, CURRENCY_ATTRIBUTE, accountReference.getCurrency()));
            }

            return specifications.toPredicate(root, query, cb);
        };
    }
}

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

package de.adorsys.psd2.xs2a.service.mapper.spi_xs2a_mappers;

import de.adorsys.psd2.xs2a.core.pis.Remittance;
import de.adorsys.psd2.xs2a.domain.pis.PeriodicPayment;
import de.adorsys.psd2.xs2a.spi.domain.payment.SpiPeriodicPayment;
import de.adorsys.psd2.xs2a.web.mapper.RemittanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Xs2aToSpiPeriodicPaymentMapper {
    private final Xs2aToSpiAmountMapper xs2aToSpiAmountMapper;
    private final Xs2aToSpiAddressMapper xs2aToSpiAddressMapper;
    private final Xs2aToSpiAccountReferenceMapper xs2aToSpiAccountReferenceMapper;
    private final Xs2aToSpiPsuDataMapper xs2aToSpiPsuDataMapper;
    private final RemittanceMapper remittanceMapper;

    public SpiPeriodicPayment mapToSpiPeriodicPayment(PeriodicPayment payment, String paymentProduct) {
        SpiPeriodicPayment periodic = new SpiPeriodicPayment(paymentProduct);
        periodic.setPaymentId(payment.getPaymentId());
        periodic.setEndToEndIdentification(payment.getEndToEndIdentification());
        periodic.setInstructionIdentification(payment.getInstructionIdentification());
        periodic.setDebtorAccount(xs2aToSpiAccountReferenceMapper.mapToSpiAccountReference(payment.getDebtorAccount()));
        periodic.setInstructedAmount(xs2aToSpiAmountMapper.mapToSpiAmount(payment.getInstructedAmount()));
        periodic.setCreditorAccount(xs2aToSpiAccountReferenceMapper.mapToSpiAccountReference(payment.getCreditorAccount()));
        periodic.setCreditorAgent(payment.getCreditorAgent());
        periodic.setCreditorName(payment.getCreditorName());
        periodic.setCreditorAddress(xs2aToSpiAddressMapper.mapToSpiAddress(payment.getCreditorAddress()));
        periodic.setRemittanceInformationUnstructured(payment.getRemittanceInformationUnstructured());
        periodic.setStartDate(payment.getStartDate());
        periodic.setEndDate(payment.getEndDate());
        periodic.setExecutionRule(payment.getExecutionRule());
        if (payment.getTransactionStatus() != null) {
            periodic.setPaymentStatus(payment.getTransactionStatus());
        }
        periodic.setFrequency(payment.getFrequency());
        periodic.setDayOfExecution(payment.getDayOfExecution());
        periodic.setRequestedExecutionTime(payment.getRequestedExecutionTime());
        periodic.setRequestedExecutionDate(payment.getRequestedExecutionDate());
        periodic.setPsuDataList(xs2aToSpiPsuDataMapper.mapToSpiPsuDataList(payment.getPsuDataList()));
        periodic.setStatusChangeTimestamp(payment.getStatusChangeTimestamp());
        periodic.setUltimateDebtor(payment.getUltimateDebtor());
        periodic.setUltimateCreditor(payment.getUltimateCreditor());
        periodic.setPurposeCode(payment.getPurposeCode());
        periodic.setRemittanceInformationStructured(remittanceMapper.mapToSpiRemittance(payment.getRemittanceInformationStructured()));
        List<Remittance> remittanceInformationStructuredArray = payment.getRemittanceInformationStructuredArray();
        if (remittanceInformationStructuredArray != null) {
            periodic.setRemittanceInformationStructuredArray(remittanceInformationStructuredArray.stream().map(remittanceMapper::mapToSpiRemittance).collect(Collectors.toList()));
        }
        periodic.setCreationTimestamp(payment.getCreationTimestamp());
        periodic.setContentType(payment.getContentType());
        periodic.setDebtorName(payment.getDebtorName());

        return periodic;
    }
}

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

package de.adorsys.psd2.xs2a.service;

import de.adorsys.psd2.event.core.model.EventType;
import de.adorsys.psd2.xs2a.core.domain.ErrorHolder;
import de.adorsys.psd2.xs2a.core.domain.TppMessageInformation;
import de.adorsys.psd2.xs2a.core.error.ErrorType;
import de.adorsys.psd2.xs2a.core.error.MessageErrorCode;
import de.adorsys.psd2.xs2a.core.error.TppMessage;
import de.adorsys.psd2.xs2a.core.mapper.ServiceType;
import de.adorsys.psd2.xs2a.core.psu.PsuIdData;
import de.adorsys.psd2.xs2a.domain.ResponseObject;
import de.adorsys.psd2.xs2a.domain.fund.FundsConfirmationRequest;
import de.adorsys.psd2.xs2a.domain.fund.FundsConfirmationResponse;
import de.adorsys.psd2.xs2a.service.context.SpiContextDataProvider;
import de.adorsys.psd2.xs2a.service.event.Xs2aEventService;
import de.adorsys.psd2.xs2a.service.mapper.spi_xs2a_mappers.SpiErrorMapper;
import de.adorsys.psd2.xs2a.service.mapper.spi_xs2a_mappers.SpiToXs2aFundsConfirmationMapper;
import de.adorsys.psd2.xs2a.service.mapper.spi_xs2a_mappers.Xs2aToSpiFundsConfirmationRequestMapper;
import de.adorsys.psd2.xs2a.service.mapper.spi_xs2a_mappers.Xs2aToSpiPiisConsentMapper;
import de.adorsys.psd2.xs2a.service.profile.AspspProfileServiceWrapper;
import de.adorsys.psd2.xs2a.spi.domain.SpiContextData;
import de.adorsys.psd2.xs2a.spi.domain.fund.SpiFundsConfirmationRequest;
import de.adorsys.psd2.xs2a.spi.domain.fund.SpiFundsConfirmationResponse;
import de.adorsys.psd2.xs2a.spi.domain.response.SpiResponse;
import de.adorsys.psd2.xs2a.spi.service.FundsConfirmationSpi;
import de.adorsys.psd2.xs2a.util.reader.TestSpiDataProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static de.adorsys.psd2.xs2a.core.error.ErrorType.PIIS_400;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FundsConfirmationServiceTest {
    private static final PsuIdData PSU_ID_DATA = new PsuIdData(null, null, null, null, null);
    private static final SpiContextData SPI_CONTEXT_DATA = TestSpiDataProvider.getSpiContextData();

    @Mock
    private AspspProfileServiceWrapper aspspProfileServiceWrapper;
    @Mock
    private Xs2aToSpiFundsConfirmationRequestMapper xs2aToSpiFundsConfirmationRequestMapper;
    @Mock
    private SpiToXs2aFundsConfirmationMapper spiToXs2aFundsConfirmationMapper;
    @Mock
    private Xs2aEventService xs2aEventService;
    @Mock
    private FundsConfirmationSpi fundsConfirmationSpi;
    @Mock
    private SpiContextDataProvider spiContextDataProvider;
    @Mock
    private SpiErrorMapper spiErrorMapper;
    @Mock
    private RequestProviderService requestProviderService;
    @Mock
    private Xs2aToSpiPiisConsentMapper xs2aToSpiPiisConsentMapper;

    @InjectMocks
    private FundsConfirmationService fundsConfirmationService;

    @Test
    void fundsConfirmation_Success_ShouldRecordEvent() {
        // Given
        when(xs2aToSpiFundsConfirmationRequestMapper.mapToSpiFundsConfirmationRequest(buildFundsConfirmationRequest()))
            .thenReturn(buildSpiFundsConfirmationRequest());
        when(requestProviderService.getPsuIdData())
            .thenReturn(PSU_ID_DATA);
        when(spiContextDataProvider.provideWithPsuIdData(PSU_ID_DATA))
            .thenReturn(SPI_CONTEXT_DATA);
        when(spiToXs2aFundsConfirmationMapper.mapToFundsConfirmationResponse(buildSpiFundsConfirmationResponse()))
            .thenReturn(buildFundsConfirmationResponse());
        when(aspspProfileServiceWrapper.isPiisConsentSupported())
            .thenReturn(false);
        when(fundsConfirmationSpi.performFundsSufficientCheck(any(), any(), any(), any()))
            .thenReturn(buildSuccessSpiResponse());
        when(xs2aToSpiPiisConsentMapper.mapToSpiPiisConsent(any()))
            .thenReturn(null);

        ArgumentCaptor<EventType> argumentCaptor = ArgumentCaptor.forClass(EventType.class);
        FundsConfirmationRequest request = buildFundsConfirmationRequest();

        // When
        fundsConfirmationService.fundsConfirmation(request);

        // Then
        verify(xs2aEventService, times(1)).recordTppRequest(argumentCaptor.capture(), any());
        assertThat(argumentCaptor.getValue()).isEqualTo(EventType.FUNDS_CONFIRMATION_REQUEST_RECEIVED);
    }

    @Test
    void fundsConfirmation_fundsConfirmationSpi_performFundsSufficientCheck_fail() {
        // Given
        ErrorHolder errorHolder = ErrorHolder
                                      .builder(PIIS_400)
                                      .tppMessages(TppMessageInformation.of(MessageErrorCode.FORMAT_ERROR))
                                      .build();
        when(xs2aToSpiFundsConfirmationRequestMapper.mapToSpiFundsConfirmationRequest(buildFundsConfirmationRequest()))
            .thenReturn(buildSpiFundsConfirmationRequest());
        when(requestProviderService.getPsuIdData())
            .thenReturn(PSU_ID_DATA);
        when(spiContextDataProvider.provideWithPsuIdData(PSU_ID_DATA))
            .thenReturn(SPI_CONTEXT_DATA);
        when(spiErrorMapper.mapToErrorHolder(any(SpiResponse.class), eq(ServiceType.PIIS)))
            .thenReturn(errorHolder);
        when(aspspProfileServiceWrapper.isPiisConsentSupported())
            .thenReturn(false);
        when(fundsConfirmationSpi.performFundsSufficientCheck(any(), any(), any(), any()))
            .thenReturn(SpiResponse.<SpiFundsConfirmationResponse>builder()
                            .error(new TppMessage(MessageErrorCode.FORMAT_ERROR))
                            .build());
        when(xs2aToSpiPiisConsentMapper.mapToSpiPiisConsent(any()))
            .thenReturn(null);

        // When
        ResponseObject<FundsConfirmationResponse> response = fundsConfirmationService.fundsConfirmation(buildFundsConfirmationRequest());

        // Then
        assertThat(response.hasError()).isTrue();
        assertThat(response.getBody()).isNull();
        assertThat(response.getError().getErrorType()).isEqualTo(ErrorType.PIIS_400);
        assertThat(response.getError().getTppMessage().getMessageErrorCode()).isEqualTo(MessageErrorCode.FORMAT_ERROR);
    }

    private FundsConfirmationRequest buildFundsConfirmationRequest() {
        return new FundsConfirmationRequest();
    }

    private SpiFundsConfirmationRequest buildSpiFundsConfirmationRequest() {
        return new SpiFundsConfirmationRequest();
    }

    private SpiFundsConfirmationResponse buildSpiFundsConfirmationResponse() {
        SpiFundsConfirmationResponse response = new SpiFundsConfirmationResponse();
        response.setFundsAvailable(true);
        return response;
    }

    private FundsConfirmationResponse buildFundsConfirmationResponse() {
        FundsConfirmationResponse response = new FundsConfirmationResponse();
        response.setFundsAvailable(true);
        return response;
    }

    private SpiResponse<SpiFundsConfirmationResponse> buildSuccessSpiResponse() {
        return SpiResponse.<SpiFundsConfirmationResponse>builder()
                   .payload(buildSpiFundsConfirmationResponse())
                   .build();
    }
}

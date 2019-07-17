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

package de.adorsys.psd2.event.service;

import de.adorsys.psd2.event.persist.EventRepository;
import de.adorsys.psd2.event.core.model.EventOrigin;
import de.adorsys.psd2.event.core.model.EventType;
import de.adorsys.psd2.event.service.mapper.AspspEventMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.OffsetDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AspspEventServiceImplTest {
    private static final OffsetDateTime START = OffsetDateTime.now().minusHours(1);
    private static final OffsetDateTime END = OffsetDateTime.now().plusHours(1);
    private static final String INSTANCE_ID = "3de76f19-1df7-44d8-b760-ca972d2f945c";
    private static final String CONSENT_ID = "fa6e687b-1ac9-4b1a-9c74-357c35c82ba1";
    private static final String PAYMENT_ID = "j-t4XyLJTzQkonfSTnyxIMc";

    @InjectMocks
    private AspspEventServiceImpl aspspEventService;

    @Mock
    private EventRepository eventRepository;
    @Spy
    private AspspEventMapper mapper = Mappers.getMapper(AspspEventMapper.class);

    @Test
    public void getEventsForPeriod() {
        when(eventRepository.getEventsForPeriod(START, END, INSTANCE_ID)).thenReturn(Collections.emptyList());

        aspspEventService.getEventsForPeriod(START, END, INSTANCE_ID);

        verify(eventRepository, times(1)).getEventsForPeriod(eq(START), eq(END), eq(INSTANCE_ID));
    }

    @Test
    public void getEventsForPeriodAndConsentId() {
        when(eventRepository.getEventsForPeriodAndConsentId(START, END, CONSENT_ID, INSTANCE_ID)).thenReturn(Collections.emptyList());

        aspspEventService.getEventsForPeriodAndConsentId(START, END, CONSENT_ID, INSTANCE_ID);

        verify(eventRepository, times(1)).getEventsForPeriodAndConsentId(eq(START), eq(END), eq(CONSENT_ID), eq(INSTANCE_ID));
    }

    @Test
    public void getEventsForPeriodAndPaymentId() {
        when(eventRepository.getEventsForPeriodAndPaymentId(START, END, PAYMENT_ID, INSTANCE_ID)).thenReturn(Collections.emptyList());

        aspspEventService.getEventsForPeriodAndPaymentId(START, END, PAYMENT_ID, INSTANCE_ID);

        verify(eventRepository, times(1)).getEventsForPeriodAndPaymentId(eq(START), eq(END), eq(PAYMENT_ID), eq(INSTANCE_ID));
    }

    @Test
    public void getEventsForPeriodAndEventOrigin() {
        when(eventRepository.getEventsForPeriodAndEventOrigin(START, END, EventOrigin.ASPSP, INSTANCE_ID)).thenReturn(Collections.emptyList());

        aspspEventService.getEventsForPeriodAndEventOrigin(START, END, EventOrigin.ASPSP, INSTANCE_ID);

        verify(eventRepository, times(1)).getEventsForPeriodAndEventOrigin(eq(START), eq(END), eq(EventOrigin.ASPSP), eq(INSTANCE_ID));
    }

    @Test
    public void getEventsForPeriodAndEventType() {
        when(eventRepository.getEventsForPeriodAndEventType(START, END, EventType.CREATE_AIS_CONSENT_REQUEST_RECEIVED, INSTANCE_ID)).thenReturn(Collections.emptyList());

        aspspEventService.getEventsForPeriodAndEventType(START, END, EventType.CREATE_AIS_CONSENT_REQUEST_RECEIVED, INSTANCE_ID);

        verify(eventRepository, times(1)).getEventsForPeriodAndEventType(eq(START), eq(END), eq(EventType.CREATE_AIS_CONSENT_REQUEST_RECEIVED), eq(INSTANCE_ID));
    }
}

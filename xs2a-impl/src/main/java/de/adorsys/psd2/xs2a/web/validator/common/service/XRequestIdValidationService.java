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

package de.adorsys.psd2.xs2a.web.validator.common.service;

import de.adorsys.psd2.xs2a.domain.TppMessageInformation;
import de.adorsys.psd2.xs2a.service.discovery.ServiceTypeDiscoveryService;
import de.adorsys.psd2.xs2a.service.mapper.psd2.ErrorType;
import de.adorsys.psd2.xs2a.service.mapper.psd2.ServiceTypeToErrorTypeMapper;
import de.adorsys.psd2.xs2a.service.validator.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static de.adorsys.psd2.xs2a.domain.MessageErrorCode.FORMAT_ERROR;
import static de.adorsys.psd2.xs2a.web.validator.constants.Xs2aHeaderConstant.X_REQUEST_ID;


/**
 * Service to be used to validate 'X-Request-ID' header in all REST calls.
 */
@Service
@RequiredArgsConstructor
public class XRequestIdValidationService implements OneHeaderValidator {

    private static final String UUID_REGEX = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}\\z";
    private static final Pattern PATTERN = Pattern.compile(UUID_REGEX, Pattern.CASE_INSENSITIVE);

    private static final String ERROR_TEXT_ABSENT_HEADER = "Header 'X-Request-ID' is missing in request";
    private static final String ERROR_TEXT_NULL_HEADER = "Header 'X-Request-ID' may not be null";
    private static final String ERROR_TEXT_BLANK_HEADER = "Header 'X-Request-ID' may not be blank";
    private static final String ERROR_TEXT_WRONG_HEADER = "Header 'X-Request-ID' has to be represented by standard 36-char UUID representation";

    private final ServiceTypeDiscoveryService serviceTypeDiscoveryService;
    private final ServiceTypeToErrorTypeMapper errorTypeMapper;

    @Override
    public ValidationResult validateHeader(Map<String, String> headers) {

        if (!headers.containsKey(X_REQUEST_ID)) {
            return ValidationResult.invalid(buildErrorType(), TppMessageInformation.of(FORMAT_ERROR, ERROR_TEXT_ABSENT_HEADER));
        }

        String xRequestId = headers.get(X_REQUEST_ID);

        if (Objects.isNull(xRequestId)) {
            return ValidationResult.invalid(buildErrorType(), TppMessageInformation.of(FORMAT_ERROR, ERROR_TEXT_NULL_HEADER));
        }

        if (StringUtils.isBlank(xRequestId)) {
            return ValidationResult.invalid(buildErrorType(), TppMessageInformation.of(FORMAT_ERROR, ERROR_TEXT_BLANK_HEADER));
        }

        if (isNonValid(xRequestId)) {
            return ValidationResult.invalid(buildErrorType(), TppMessageInformation.of(FORMAT_ERROR, ERROR_TEXT_WRONG_HEADER));
        }

        return ValidationResult.valid();
    }

    private ErrorType buildErrorType() {
        return errorTypeMapper.mapToErrorType(serviceTypeDiscoveryService.getServiceType(), FORMAT_ERROR.getCode());
    }

    private boolean isNonValid(String xRequestId) {
        return !PATTERN.matcher(xRequestId).matches();
    }
}

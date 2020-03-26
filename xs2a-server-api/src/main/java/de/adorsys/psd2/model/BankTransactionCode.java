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

package de.adorsys.psd2.model;

import io.swagger.annotations.ApiModel;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * Bank transaction code as used by the ASPSP and using the sub elements of this structured code defined by ISO 20022.   This code type is concatenating the three ISO20022 Codes    * Domain Code,    * Family Code, and    * SubFamiliy Code  by hyphens, resulting in &#39;DomainCode&#39;-&#39;FamilyCode&#39;-&#39;SubFamilyCode&#39;. For standing order reports the following codes are applicable:   * \&quot;PMNT-ICDT-STDO\&quot; for credit transfers,   * \&quot;PMNT-IRCT-STDO\&quot;  for instant credit transfers   * \&quot;PMNT-ICDT-XBST\&quot; for cross-border credit transfers   * \&quot;PMNT-IRCT-XBST\&quot; for cross-border real time credit transfers and   * \&quot;PMNT-MCOP-OTHR\&quot; for specific standing orders which have a dynamical amount to move left funds e.g. on month end to a saving account
 */
@ApiModel(description = "Bank transaction code as used by the ASPSP and using the sub elements of this structured code defined by ISO 20022.   This code type is concatenating the three ISO20022 Codes    * Domain Code,    * Family Code, and    * SubFamiliy Code  by hyphens, resulting in 'DomainCode'-'FamilyCode'-'SubFamilyCode'. For standing order reports the following codes are applicable:   * \"PMNT-ICDT-STDO\" for credit transfers,   * \"PMNT-IRCT-STDO\"  for instant credit transfers   * \"PMNT-ICDT-XBST\" for cross-border credit transfers   * \"PMNT-IRCT-XBST\" for cross-border real time credit transfers and   * \"PMNT-MCOP-OTHR\" for specific standing orders which have a dynamical amount to move left funds e.g. on month end to a saving account ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-16T13:49:16.891743+02:00[Europe/Kiev]")

public class BankTransactionCode {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankTransactionCode {\n");

        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}


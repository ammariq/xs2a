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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * NextGenPSD2 specific definition of reporting error information in case of a HTTP error code 401.
 */
@ApiModel(description = "NextGenPSD2 specific definition of reporting error information in case of a HTTP error code 401. ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-16T13:49:16.891743+02:00[Europe/Kiev]")

public class Error405NGPISCANC {
    @JsonProperty("tppMessages")
    @Valid
    private List<TppMessage405PISCANC> tppMessages = null;

    @JsonProperty("_links")
    private Map _links = null;

    public Error405NGPISCANC tppMessages(List<TppMessage405PISCANC> tppMessages) {
        this.tppMessages = tppMessages;
        return this;
    }

    public Error405NGPISCANC addTppMessagesItem(TppMessage405PISCANC tppMessagesItem) {
        if (this.tppMessages == null) {
            this.tppMessages = new ArrayList<>();
        }
        this.tppMessages.add(tppMessagesItem);
        return this;
    }

    /**
     * Get tppMessages
     *
     * @return tppMessages
     **/
    @ApiModelProperty(value = "")

    @Valid


    @JsonProperty("tppMessages")
    public List<TppMessage405PISCANC> getTppMessages() {
        return tppMessages;
    }

    public void setTppMessages(List<TppMessage405PISCANC> tppMessages) {
        this.tppMessages = tppMessages;
    }

    public Error405NGPISCANC _links(Map _links) {
        this._links = _links;
        return this;
    }

    /**
     * Get _links
     *
     * @return _links
     **/
    @ApiModelProperty(value = "")

    @Valid


    @JsonProperty("_links")
    public Map getLinks() {
        return _links;
    }

    public void setLinks(Map _links) {
        this._links = _links;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Error405NGPISCANC error405NGPISCANC = (Error405NGPISCANC) o;
        return Objects.equals(this.tppMessages, error405NGPISCANC.tppMessages) &&
                   Objects.equals(this._links, error405NGPISCANC._links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tppMessages, _links);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Error405NGPISCANC {\n");

        sb.append("    tppMessages: ").append(toIndentedString(tppMessages)).append("\n");
        sb.append("    _links: ").append(toIndentedString(_links)).append("\n");
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


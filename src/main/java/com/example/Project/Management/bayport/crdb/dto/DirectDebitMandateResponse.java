package com.example.Project.Management.bayport.crdb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DirectDebitMandateResponse {
    @JsonProperty("ResponseHeader")
    private ResponseHeader responseHeader;

    @JsonProperty("ResponseBody")
    private ResponseBody responseBody;

    @Data
    public static class ResponseHeader {
        @JsonProperty("StatusCode")
        private Integer statusCode;

        @JsonProperty("StatusMessage")
        private String statusMessage;

        @JsonProperty("StatusDesc")
        private String statusDesc;

        @JsonProperty("RequestId")
        private String requestId;

        @JsonProperty("ErrorCode")
        private String errorCode;

        @JsonProperty("ErrorMessage")
        private String errorMessage;

        @JsonProperty("ErrorMetadata")
        private Object errorMetadata;
    }

    @Data
    public static class ResponseBody {
        @JsonProperty("RequestId")
        private String requestId;

        @JsonProperty("Reference")
        private Integer reference;
    }
}
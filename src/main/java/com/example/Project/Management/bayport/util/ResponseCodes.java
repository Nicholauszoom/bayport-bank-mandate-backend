package com.example.Project.Management.bayport.util;


import java.util.HashMap;
import java.util.Map;

public class ResponseCodes {
    // Define response code constants
    public static final int SUCCESSFUL_RECEIVED = 8000;
    public static final int REQUIRED_HEADER_MISSING = 8002;
    public static final int INVALID_FINANCIAL_SERVICE_PROVIDER = 8003;
    public static final int INVALID_BANK_ACCOUNT = 8007;
    public static final int INVALID_SIGNATURE = 8009;
    public static final int ERROR_PROCESSING_REQUEST = 8011;
    public static final int VOTE_CODE_NOT_EXIST = 8017;
    public static final int INVALID_APPLICATION_NUMBER = 8013;
    public static final int UNAUTHORIZED = 8001;
    public static final int SERVICE_PROVIDER_NOT_ACTIVE = 8004;
    public static final int DUPLICATE_REQUEST = 8006;
    public static final int BANK_ACCOUNT_NOT_ACTIVE = 8008;
    public static final int INVALID_SIGNATURE_CONFIGURATION = 8010;
    public static final int REQUEST_CANNOT_COMPLETED = 8012;
    public static final int INVALID_CODE_MISMATCH = 8014;
    public static final int CHECK_NUMBER_NOT_EXIST = 8016;
    public static final int INVALID_PRODUCT_CODE = 8018;
    public static final int PAYMENT_REFERENCE_NOT_EXIST = 8020;
    public static final int PRODUCT_AMOUNT_EXCEPTION_CODE = 8021;
    public static final int INVALID_PRODUCT_TENURE_CODE = 8022;
    public static final int INVALID_PRODUCT_RETIREMENT_DATE_CODE = 8023;
    public static final int INVALID_PRODUCT_RETIREMENT_PERIOD_CODE = 8024;
    public static final int INVALID_LOAN_CODE = 8025;
    public static final int INVALID_TEAMS_LOAN_CODE=8026;

    public static final int DECCOMMISSION_SUCCESSFUL=8027;
    public static final int INTERNAL_SERVER_ERROR = 500 ;

    public static final int TRUE =1;

    public static final int FALSE =0;

    // Map to store response code descriptions
    private static final Map<Integer, String> codeDescriptions = new HashMap<>();

    // Static initializer to populate codeDescriptions map
    static {
        codeDescriptions.put(SUCCESSFUL_RECEIVED, "Successful/Received");
        codeDescriptions.put(REQUIRED_HEADER_MISSING, "Required header is not given");
        codeDescriptions.put(INVALID_FINANCIAL_SERVICE_PROVIDER, "Invalid Financial service provider code");
        codeDescriptions.put(INVALID_BANK_ACCOUNT, "General Failure Invalid bank account");
        codeDescriptions.put(INVALID_SIGNATURE, "Invalid Signature");
        codeDescriptions.put(ERROR_PROCESSING_REQUEST, "Error on processing request ");
        codeDescriptions.put(VOTE_CODE_NOT_EXIST, "Vote code does not exist");
        codeDescriptions.put(INVALID_APPLICATION_NUMBER, "Invalid Application number");
        codeDescriptions.put(UNAUTHORIZED, "Unauthorized");
        codeDescriptions.put(SERVICE_PROVIDER_NOT_ACTIVE, "Service provider is not active");
        codeDescriptions.put(DUPLICATE_REQUEST, "Duplicate Request");
        codeDescriptions.put(BANK_ACCOUNT_NOT_ACTIVE, "Bank account not active");
        codeDescriptions.put(INVALID_SIGNATURE_CONFIGURATION, "Invalid Signature Configuration missing one of parameter passphrase, key alias, filename)");
        codeDescriptions.put(REQUEST_CANNOT_COMPLETED, "Request cannot completed at this time, try later");
        codeDescriptions.put(INVALID_CODE_MISMATCH, "Invalid code, mismatch of supplied code on information and header");
        codeDescriptions.put(CHECK_NUMBER_NOT_EXIST, "Check Number does not exist");
        codeDescriptions.put(INVALID_PRODUCT_CODE, "Invalid product code");
        codeDescriptions.put(PAYMENT_REFERENCE_NOT_EXIST, "Payment reference number does not exist");
        codeDescriptions.put(PRODUCT_AMOUNT_EXCEPTION_CODE, "Loan charges request failed. Requested amount cannot exceed max or min product amount");
        codeDescriptions.put(INVALID_PRODUCT_TENURE_CODE, "Loan charges request failed. Tenure cannot exceed max or min tenure");
        codeDescriptions.put(INVALID_PRODUCT_RETIREMENT_DATE_CODE, "Loan charges request failed. retirement date cannot be less than 0");
        codeDescriptions.put(INVALID_PRODUCT_RETIREMENT_PERIOD_CODE, "Loan charges request failed. retirement date is too close to the end of the loan repayment period ");
        codeDescriptions.put(INVALID_LOAN_CODE, "not found the loan requested");
        codeDescriptions.put(INVALID_TEAMS_LOAN_CODE, "terms not allow to cancel this loan");
        codeDescriptions.put(DECCOMMISSION_SUCCESSFUL, "product decommission successful");
        codeDescriptions.put(TRUE, "true");
        codeDescriptions.put(FALSE, "false");

    }

    // Method to get description for a given response code
    public static String getDescription(int code) {
        return codeDescriptions.getOrDefault(code, "Unknown code");
    }
}


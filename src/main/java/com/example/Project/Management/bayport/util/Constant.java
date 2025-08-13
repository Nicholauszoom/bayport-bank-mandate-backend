package com.example.Project.Management.bayport.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class Constants {

    //Connection Times
    public final static Long CONNECT_TIMEOUT = 10000L;
    public final static Long RETRY_CONNECT_TIMEOUT = 30000L;

    //Api Categories
    public final static Long API_CATEG_PRODUCT_SUBMISSION_REQ = 1L;

    //Record Status
    public static final Long RECORD_STATUS_ACTIVE = 1L;
    public static final Long RECORD_STATUS_INACTIVE = 0L;

    //Log Levels
    public final static Integer LOG_DEBUG = 1;
    public final static Integer LOG_INFO = 2;
    public final static Integer LOG_WARN = 3;
    public final static Integer LOG_ERROR = 4;
    public final static Integer LOG_FATAL = 5;

    //Other
    public static final Integer DEFAULT_SYS_BUFFER_TIME = 600;
    public static final String DEFAULT_SUCCESS_STATUS = "1001";
    public static final Long DEFAULT_SYS_USER_ID = 1L;
    public static final String DEFAULT_SYS_USER_NAME = "eMkopo Admin";

    // message types
    public static final String PRODUCT_RESPONSE ="RESPONSE";

    public static final String PRODUCT_DETAIL= "PRODUCT_DETAIL";

    public static final String PRODUCT_REGISTRATION_DETAIL = "PRODUCT_DETAIL";

    public static final String PRODUCT_DECOMMISSION="PRODUCT_DECOMMISSION";

    public static final String LOAN_CHARGES_RESPONSE ="LOAN_CHARGES_RESPONSE";

    public static final String LOAN_CHARGES_REQUEST ="LOAN_CHARGES_REQUEST";

    public static final String PAY_0FF_BALANCE_REQUEST="PAY_0FF_BALANCE_REQUEST";

    public static final String TOP_UP_PAY_0FF_BALANCE_REQUEST="TOP_UP_PAY_0FF_BALANCE_REQUEST";

    public static final String FULL_REPAYMENT_0FF_BALANCE_REQUEST="FULL_REPAYMENT_0FF_BALANCE_REQUEST";

    public static final String PARTIAL_REPAYMENT_0FF_BALANCE_REQUEST="PARTIAL_REPAYMENT_0FF_BALANCE_REQUEST";

    public static final String LOAN_CANCELLATION_NOTIFICATION="LOAN_CANCELLATION_NOTIFICATION";

    public static final String ACCOUNT_VALIDATION="ACCOUNT_VALIDATION";

    public static final String LOAN_OFFER_REQUEST="LOAN_OFFER_REQUEST";

    public static final String TOP_UP_OFFER_REQUEST="TOP_UP_OFFER_REQUEST";

    public static final String TAKEOVER_EMPLOYEE_DETAILS_TO_FSP="TAKEOVER_EMPLOYEE_DETAILS_TO_FSP";

    public static final String LOAN_INITIAL_APPROVAL_NOTIFICATION="LOAN_INITIAL_APPROVAL_NOTIFICATION";

    public static final String LOAN_FINAL_APPROVAL_NOTIFICATION= "LOAN_FINAL_APPROVAL_NOTIFICATION";

    public static final String LOAN_DISBURSEMENT_NOTIFICATION_TAKEOVER_DISBURSEMENT_NOTIFICATION="LOAN_DISBURSEMENT_NOTIFICATION || TAKEOVER_DISBURSEMENT_NOTIFICATION";

    public static final String LOAN_DISBURSEMENT_NOTIFICATION ="LOAN_DISBURSEMENT_NOTIFICATION";

    public static final String LOAN_DISBURSEMENT_FAILURE_NOTIFICATION ="LOAN_DISBURSEMENT_FAILURE_NOTIFICATION";

    public static final String LOAN_CANCELLATION_APPROVAL_NOTIFICATION="LOAN_CANCELLATION_APPROVAL_NOTIFICATIO";
    public static final String LOAN_RESTRUCTURING_NOTIFICATION ="LOAN_RESTRUCTURING_NOTIFICATION";

    public static final String LOAN_TAKEOVER_DETAILS_FROM_FSP ="LOAN_TAKEOVER_DETAILS_FROM_FSP";

    public static final String NOTIFICATION_FROM_FSP1_TO_EMPLOYER = "NOTIFICATION_FROM_FSP1_TO_EMPLOYER";

    public static final String TAKOVER_DISBURSEMENT_NOTIFICATION ="TAKOVER_DISBURSEMENT_NOTIFICATION";

    public static final String LOAN_RESTRUCTURE_BALANCE_RESPONSE = "LOAN_RESTRUCTURE_BALANCE_RESPONSE";

    public static final String FULL_LOAN_REPAYMENT_NOTIFICATION ="FULL_LOAN_REPAYMENT_NOTIFICATIO";

    public static final String PARTIAL_REPAYMENT_0FF_BALANCE_RESPONSE = "PARTIAL_REPAYMENT_0FF_BALANCE_RESPONSE";

    public static final String PARTIAL_LOAN_REPAYMENT_NOTIFICATION ="PARTIAL_LOAN_REPAYMENT_NOTIFICATION";
    public static final String FULL_REPAYMENT_0FF_BALANCE_RESPONSE ="FULL_REPAYMENT_0FF_BALANCE_RESPONSE";

    public static final String REQUEST_DIGITAL_SIGNATURE_CREATE ="REQUEST_DIGITAL_SIGNATURE_CREATE";

    public static final String LOAN_PAY_0FF_BALANCE_RESPONSE ="LOAN_PAY_0FF_BALANCE_RESPONSE";

    public static final String DIGITAL_SIGNATURE_VERIFICATION ="DIGITAL_SIGNATURE_VERIFICATION";
    public static final String LOAN_TAKEOVER_APPROVAL_NOTIFICATION_FSP2 ="LOAN_TAKEOVER_APPROVAL_NOTIFICATION_FSP2";

    public static final String ACCOUNT_VALIDATION_RESPONSE ="ACCOUNT_VALIDATION_RESPONSE";
    public static final String DEFAULTER_DETAILS_TO_EMPLOYER ="DEFAULTER_DETAILS_TO_EMPLOYER";

    public static final String FSP_BRANCHES = "FSP_BRANCHES";

    public static final String LOAN_TOP_UP_BALANCE_RESPONSE ="LOAN_TOP_UP_BALANCE_RESPONSE";

    public static final String LOAN_REGISTRATION_DETAIL = "LOAN_DETAIL";

    public static final String LOAN_LIQUIDATION_NOTIFICATION = "LOAN_LIQUIDATION_NOTIFICATION";

    public static final String PAYMENT_ACKNOWLEDGMENT_NOTIFICATION = "PAYMENT_ACKNOWLEDGMENT_NOTIFICATION";

    public static final String LOAN_REGISTRATION_NOTIFICATION_DETAIL = "LOAN_APPROVAL_NOTIFICATION_DETAIL";

    public static final String LOAN_DISBURSEMENT_DETAIL = "LOAN_DISBURSEMENT_DETAIL";

    public static final String LOAN_TOP_UP_PAY_0FF_BALANCE_RESPONSE ="LOAN_TOP_UP_BALANCE_RESPONSE";

    // status actions
    public static final String LOAN_PROCESSING_ACTION = "LOAN_ON_PROCESS";

    public static final String LOAN_DISBURSEMENT_ACTION = "LOAN_DISBURSSED";

    public static final String LOAN_CANCELLED_ACTION = "LOAN_CANCELLED";
    public static final String LOAN_TOP_UP_PROCESSING_ACTION = "LOAN_TOP_UP_PROCESSING_ACTION";


    public static final String LOAN_FINAL_APPROVAL = "LOAN_APPROVED";

    public static final String LOAN_FINAL_APPROVAL_NOTIFICATION_APPROVED = "LOAN_APPROVED";

    public static final String LOAN_FINAL_APPROVAL_NOTIFICATION_REJECTED = "LOAN_REJECTED";
    public static final String LOAN_REJECTION_ACTION = "LOAN_REJECTED";

    public static final String LOAN_FAIL_DISBURSEMENT_ACTION = "FAIL_DISBURSEMENT";

    public static final String SUCCESS_DISBURSMENT_ACTION = "SUCCESS_DISBURSEMENT";

    public static final String FAIL_DISBURSMENT_ACTION = "FAIL_DISBURSEMENT";

    public static final String UTUMISHI_RECEIVER = "ESS_UTUMISHI";
    public static final String DFLEX_ENGINE_NAME = "DFLEX_ENGINE";

    public static final String LOAN_RESTRUCTURE_AFFORDABILITY_RESPONSE ="LOAN_RESTRUCTURE_AFFORDABILITY_RESPONSE";

    public static final String FSP_RECEIVER = "FSP";

    public static final String UTUMISHI_RESPONSE = "RESPONSE";

    public static final int RECORD_ACTIVE = 1;

    public static final String[] IP_HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    public static final int INVALID_AMOUNT_CODE = 8005 ;
    public static final int INVALID_TENURE_CODE = 8006;
    public static final int INVALID_RETIREMENT_DATE_CODE = 8007;
    public static final int INVALID_RETIREMENT_PERIOD_CODE = 8008;
    public static final int INTERNAL_SERVER_ERROR_CODE = 500;

    public static final String SWIFT_CODE = "SBICTZTXXXX";
    public static final String FSP_BANK_ACCOUNT = "9120001208495";
    public static final String FSP_BANK_ACCOUNT_NAME = "BAYPORT FINANCIAL SERVICES (T) LTD";
    public static final String LOAN_TAKEOVER_BALANCE_RESPONSE = "LOAN_TAKEOVER_BALANCE_RESPONSE";
    public static final String TAKEOVER_DISBURSEMENT_NOTIFICATION = "TAKEOVER_DISBURSEMENT_NOTIFICATION";
}

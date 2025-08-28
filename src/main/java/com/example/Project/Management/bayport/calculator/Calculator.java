package com.example.Project.Management.bayport.calculator;

import com.example.Project.Management.bayport.charge.dto.charges.LoanChargesApiReqDetailDto;
import com.example.Project.Management.bayport.charge.dto.charges.LoanChargesApiRespDetailDto;
import com.example.Project.Management.bayport.charge.entity.ClientRv;
import com.example.Project.Management.bayport.charge.entity.Price;
import com.example.Project.Management.bayport.charge.service.PriceClientRvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Slf4j
public class Calculator {
    private final PriceClientRvService priceClientRvService;

    public Calculator(PriceClientRvService priceClientRvService) {
        this.priceClientRvService = priceClientRvService;
    }

    public LoanChargesApiRespDetailDto calculateCharges(LoanChargesApiReqDetailDto loanChargesApiReqDetailDto) {

        //New Loan or Refinance
        LoanChargesApiRespDetailDto loanChargesApiRespDetailDto = new LoanChargesApiRespDetailDto();
        String checkNumber = loanChargesApiReqDetailDto.getCheckNumber();
        ClientRv client = priceClientRvService.findByCheckNumber(checkNumber);

        if (client == null) {
            // New Loan

            BigDecimal requestedAmount =  loanChargesApiReqDetailDto.getRequestedAmount();
            int tenure = loanChargesApiReqDetailDto.getTenure();
            BigDecimal desiredDeductibleAmount = loanChargesApiReqDetailDto.getDesiredDeductibleAmount();
            BigDecimal deductableAmount = loanChargesApiReqDetailDto.getDeductibleAmount();
            String description = "New Loan";
            BigDecimal VATPercent = BigDecimal.valueOf(0.18);

            LoanChargesApiRespDetailDto respDetailDto = getLoanChargesApiRespDetailDto(loanChargesApiRespDetailDto, requestedAmount,
                    tenure, desiredDeductibleAmount, deductableAmount, description, VATPercent);
            loanChargesApiRespDetailDto = respDetailDto;

        } else {
            // For Refinance Loan

            BigDecimal requestedAmount =  loanChargesApiReqDetailDto.getRequestedAmount();
            int tenure = loanChargesApiReqDetailDto.getTenure();
            BigDecimal desiredDeductibleAmount = loanChargesApiReqDetailDto.getDesiredDeductibleAmount();
            BigDecimal deductableAmount = loanChargesApiReqDetailDto.getDeductibleAmount();
            String description = "Refinance Loan";
            BigDecimal VATPercent = BigDecimal.valueOf(0.18);

            LoanChargesApiRespDetailDto respDetailDto = getLoanChargesApiRespDetailDto(loanChargesApiRespDetailDto, requestedAmount,
                    tenure, desiredDeductibleAmount, deductableAmount, description, VATPercent);
            loanChargesApiRespDetailDto = respDetailDto;
        }
        return loanChargesApiRespDetailDto;
    }

    //calculate take home
    public BigDecimal getTakeHomeAmount(BigDecimal RV,BigDecimal otherCharges, BigDecimal netLoanAmount) {
        BigDecimal takeHomeAmount = netLoanAmount
                .subtract(otherCharges)
                .subtract(RV);
        return takeHomeAmount;
    }

    //calculate ESS TotaInsurance
    public BigDecimal getEssTotalInsurance(BigDecimal insuranceAmount, BigDecimal processingFee) {
        BigDecimal totalEssInsuranceAmount = insuranceAmount
                .add(processingFee);
        return totalEssInsuranceAmount;
    }


    public LoanChargesApiRespDetailDto
    getLoanChargesApiRespDetailDto(LoanChargesApiRespDetailDto loanChargesApiRespDetailDto,
                                   BigDecimal requestedAmount, int tenure, BigDecimal desiredDeductibleAmount,
                                   BigDecimal deductableAmount, String description, BigDecimal VATPercent) {
        //        *SCENARIO 1:*
        if (requestedAmount.compareTo(BigDecimal.ZERO) > 0
                && desiredDeductibleAmount.compareTo(BigDecimal.ZERO) == 0
                && tenure == 0) {


            Price priceDetail = priceClientRvService.findWithMaxLoanTermAndDescription(description);
            BigDecimal factor = BigDecimal.valueOf(priceDetail.getFactor());
            BigDecimal maxTerm = BigDecimal.valueOf(priceDetail.getLoanTerm());

            //adminFee
            BigDecimal adminFee = BigDecimal
                    .valueOf(priceDetail.getAdminPercentage());

            //insurance
            BigDecimal insurance = BigDecimal
                    .valueOf(priceDetail.getInsurancePercentage());

            //deductibleAmount
            BigDecimal deductibleAmount = requestedAmount.multiply(factor)
                    .divide(maxTerm, 2, RoundingMode.HALF_UP);


            //insuranceAmount
            BigDecimal insuranceAmount =
                    deductibleAmount
                            .divide(insurance.add(BigDecimal.ONE), 2, RoundingMode.HALF_UP)
                            .multiply(insurance)
                            .multiply(maxTerm)
                            .setScale(2, RoundingMode.HALF_UP);

            //NetLoanAmount = requestedAmount
            BigDecimal netLoan = requestedAmount;

            //Processing Fees
            BigDecimal processingFee = requestedAmount
                    .multiply(adminFee.add(BigDecimal.ONE))
                    .subtract(requestedAmount)
                    .setScale(2, RoundingMode.HALF_UP);


            //totalAmountToPay
            BigDecimal totalAmountToPay = deductibleAmount
                    .multiply(maxTerm)
                    .setScale(2, RoundingMode.HALF_UP);

            //Interest Amount
            BigDecimal interestAmount = totalAmountToPay
                    .subtract(netLoan)
                    .subtract(processingFee)
                    .subtract(insuranceAmount)
                    .setScale(2, RoundingMode.HALF_UP);

            //other charges
            BigDecimal otherCharges = ((requestedAmount
                    .multiply(adminFee.add(BigDecimal.ONE)))
                    .subtract(requestedAmount))
                    .multiply(VATPercent)
                    .setScale(2, RoundingMode.HALF_UP);

            //Eligible Amount
            BigDecimal eligibleAmount = BigDecimal.ZERO;

            //MonthlyReturnAmount
            BigDecimal monthlyReturnAmount = deductibleAmount;

            loanChargesApiRespDetailDto.setDesiredDeductibleAmount(deductibleAmount);
            loanChargesApiRespDetailDto.setDesiredDeductibleAmount(deductibleAmount);
            loanChargesApiRespDetailDto.setTotalInsurance(insuranceAmount);
            loanChargesApiRespDetailDto.setTotalProcessingFee(processingFee);
            loanChargesApiRespDetailDto.setTotalInterestRateAmount(interestAmount);
            loanChargesApiRespDetailDto.setOtherCharges(otherCharges);
            loanChargesApiRespDetailDto.setNetLoanAmount(netLoan);
            loanChargesApiRespDetailDto.setTotalAmountToPay(totalAmountToPay);
            loanChargesApiRespDetailDto.setTenure(maxTerm.intValue());
            loanChargesApiRespDetailDto.setEligibleAmount(eligibleAmount);
            loanChargesApiRespDetailDto.setMonthlyReturnAmount(monthlyReturnAmount);

        }


        //            //*SCENARIO 2:*
        //Akiweka both loan amount na deduction=(loan amount*factor ya max term)/max term to get deduction (ignore desirable deduction alioweka)
        else if (requestedAmount.compareTo(BigDecimal.ZERO) > 0
                && desiredDeductibleAmount.compareTo(BigDecimal.ZERO) > 0
                && tenure == 0) {

            Price priceDetail = priceClientRvService.findWithMaxLoanTermAndDescription(description);
            BigDecimal factor = BigDecimal.valueOf(priceDetail.getFactor());
            BigDecimal term = BigDecimal.valueOf(priceDetail.getLoanTerm());

            //adminFee
            BigDecimal adminFee = BigDecimal
                    .valueOf(priceDetail.getAdminPercentage());

            //insurance
            BigDecimal insurance = BigDecimal
                    .valueOf(priceDetail.getInsurancePercentage());

            //deductibleAmount
            BigDecimal deductibleAmount = requestedAmount.multiply(factor)
                    .divide(term, 2, RoundingMode.HALF_UP);

            //insuranceAmount
            BigDecimal insuranceAmount =
                    deductibleAmount
                            .divide(insurance.add(BigDecimal.ONE), 2, RoundingMode.HALF_UP)
                            .multiply(insurance)
                            .multiply(term)
                            .setScale(2, RoundingMode.HALF_UP);

            //NetLoanAmount = requestedAmount
            BigDecimal netLoan = requestedAmount;

            //Processing Fees
            BigDecimal processingFee = requestedAmount
                    .multiply(adminFee.add(BigDecimal.ONE))
                    .subtract(requestedAmount)
                    .setScale(2, RoundingMode.HALF_UP);


            //totalAmountToPay
            BigDecimal totalAmountToPay = deductibleAmount
                    .multiply(term)
                    .setScale(2, RoundingMode.HALF_UP);

            //Interest Amount
            BigDecimal interestAmount = totalAmountToPay
                    .subtract(netLoan)
                    .subtract(processingFee)
                    .subtract(insuranceAmount)
                    .setScale(2, RoundingMode.HALF_UP);

            //other charges
            BigDecimal otherCharges = ((requestedAmount
                    .multiply(adminFee.add(BigDecimal.ONE)))
                    .subtract(requestedAmount))
                    .multiply(VATPercent)
                    .setScale(2, RoundingMode.HALF_UP);

            //Eligible Amount
            BigDecimal eligibleAmount = BigDecimal.ZERO;

            //MonthlyReturnAmount
            BigDecimal monthlyReturnAmount = deductibleAmount;

            loanChargesApiRespDetailDto.setDesiredDeductibleAmount(deductibleAmount);
            loanChargesApiRespDetailDto.setDesiredDeductibleAmount(deductibleAmount);
            loanChargesApiRespDetailDto.setTotalInsurance(insuranceAmount);
            loanChargesApiRespDetailDto.setTotalProcessingFee(processingFee);
            loanChargesApiRespDetailDto.setTotalInterestRateAmount(interestAmount);
            loanChargesApiRespDetailDto.setOtherCharges(otherCharges);
            loanChargesApiRespDetailDto.setNetLoanAmount(netLoan);
            loanChargesApiRespDetailDto.setTotalAmountToPay(totalAmountToPay);
            loanChargesApiRespDetailDto.setTenure(term.intValue());
            loanChargesApiRespDetailDto.setEligibleAmount(eligibleAmount);
            loanChargesApiRespDetailDto.setMonthlyReturnAmount(monthlyReturnAmount);
        }




        //            //*SCENARIO 3:*
        else if (requestedAmount.compareTo(BigDecimal.ZERO) > 0
                && desiredDeductibleAmount.compareTo(BigDecimal.ZERO) == 0
                && tenure > 2) {

            Price priceDetail = priceClientRvService.findWithXLoanTermAndDescription(description, tenure);
            BigDecimal factor = BigDecimal.valueOf(priceDetail.getFactor());
            BigDecimal term = BigDecimal.valueOf(priceDetail.getLoanTerm());

            //adminFee
            BigDecimal adminFee = BigDecimal
                    .valueOf(priceDetail.getAdminPercentage());

            //insurance
            BigDecimal insurance = BigDecimal
                    .valueOf(priceDetail.getInsurancePercentage());

            //deductibleAmount
            BigDecimal deductibleAmount = requestedAmount.multiply(factor)
                    .divide(term, 2, RoundingMode.HALF_UP);


            //insuranceAmount
            BigDecimal insuranceAmount =
                    deductibleAmount
                            .divide(insurance.add(BigDecimal.ONE), 2, RoundingMode.HALF_UP)
                            .multiply(insurance)
                            .multiply(term)
                            .setScale(2, RoundingMode.HALF_UP);

            //NetLoanAmount = requestedAmount
            BigDecimal netLoan = requestedAmount;

            //Processing Fees
            BigDecimal processingFee = requestedAmount
                    .multiply(adminFee.add(BigDecimal.ONE))
                    .subtract(requestedAmount)
                    .setScale(2, RoundingMode.HALF_UP);


            //totalAmountToPay
            BigDecimal totalAmountToPay = deductibleAmount
                    .multiply(term)
                    .setScale(2, RoundingMode.HALF_UP);

            //Interest Amount
            BigDecimal interestAmount = totalAmountToPay
                    .subtract(netLoan)
                    .subtract(processingFee)
                    .subtract(insuranceAmount)
                    .setScale(2, RoundingMode.HALF_UP);

            //other charges
            BigDecimal otherCharges = ((requestedAmount
                    .multiply(adminFee.add(BigDecimal.ONE)))
                    .subtract(requestedAmount))
                    .multiply(VATPercent)
                    .setScale(2, RoundingMode.HALF_UP);

            //Eligible Amount
            BigDecimal eligibleAmount = BigDecimal.ZERO;

            //MonthlyReturnAmount
            BigDecimal monthlyReturnAmount = deductibleAmount;

            loanChargesApiRespDetailDto.setDesiredDeductibleAmount(deductibleAmount);
            loanChargesApiRespDetailDto.setDesiredDeductibleAmount(deductibleAmount);
            loanChargesApiRespDetailDto.setTotalInsurance(insuranceAmount);
            loanChargesApiRespDetailDto.setTotalProcessingFee(processingFee);
            loanChargesApiRespDetailDto.setTotalInterestRateAmount(interestAmount);
            loanChargesApiRespDetailDto.setOtherCharges(otherCharges);
            loanChargesApiRespDetailDto.setNetLoanAmount(netLoan);
            loanChargesApiRespDetailDto.setTotalAmountToPay(totalAmountToPay);
            loanChargesApiRespDetailDto.setTenure(term.intValue());
            loanChargesApiRespDetailDto.setEligibleAmount(eligibleAmount);
            loanChargesApiRespDetailDto.setMonthlyReturnAmount(monthlyReturnAmount);
        }

        // *SCENARIO 4:*
        // Akiweka installment pekee=(desireble installment*max tenure)/factor ya max tenure
        else if (requestedAmount.compareTo(BigDecimal.ZERO) == 0
                && desiredDeductibleAmount.compareTo(BigDecimal.ZERO) > 0
                && tenure == 0) {

            Price priceDetail = priceClientRvService.findWithMaxLoanTermAndDescription(description);
            BigDecimal factor = BigDecimal.valueOf(priceDetail.getFactor());
            BigDecimal term = BigDecimal.valueOf(priceDetail.getLoanTerm());

            //adminFee
            BigDecimal adminFee = BigDecimal
                    .valueOf(priceDetail.getAdminPercentage());

            //insurance
            BigDecimal insurance = BigDecimal
                    .valueOf(priceDetail.getInsurancePercentage());

            //deductibleAmount
            BigDecimal deductibleAmount =desiredDeductibleAmount;

            //requested Amount
            requestedAmount = (desiredDeductibleAmount.multiply(term))
                    .divide(factor, 2, RoundingMode.HALF_UP);

            //insuranceAmount
            BigDecimal insuranceAmount =
                    deductibleAmount
                            .divide(insurance.add(BigDecimal.ONE), 2, RoundingMode.HALF_UP)
                            .multiply(insurance)
                            .multiply(term)
                            .setScale(2, RoundingMode.HALF_UP);

            //NetLoanAmount = requestedAmount
            BigDecimal netLoan = requestedAmount;

            //Processing Fees
            BigDecimal processingFee = requestedAmount
                    .multiply(adminFee.add(BigDecimal.ONE))
                    .subtract(requestedAmount)
                    .setScale(2, RoundingMode.HALF_UP);


            //totalAmountToPay
            BigDecimal totalAmountToPay = deductibleAmount
                    .multiply(term)
                    .setScale(2, RoundingMode.HALF_UP);

            //Interest Amount
            BigDecimal interestAmount = totalAmountToPay
                    .subtract(netLoan)
                    .subtract(processingFee)
                    .subtract(insuranceAmount)
                    .setScale(2, RoundingMode.HALF_UP);

            //other charges
            BigDecimal otherCharges = ((requestedAmount
                    .multiply(adminFee.add(BigDecimal.ONE)))
                    .subtract(requestedAmount))
                    .multiply(VATPercent)
                    .setScale(2, RoundingMode.HALF_UP);

            //Eligible Amount
            BigDecimal eligibleAmount = BigDecimal.ZERO;

            //MonthlyReturnAmount
            BigDecimal monthlyReturnAmount = deductibleAmount;

            loanChargesApiRespDetailDto.setDesiredDeductibleAmount(deductibleAmount);
            loanChargesApiRespDetailDto.setDesiredDeductibleAmount(deductibleAmount);
            loanChargesApiRespDetailDto.setTotalInsurance(insuranceAmount);
            loanChargesApiRespDetailDto.setTotalProcessingFee(processingFee);
            loanChargesApiRespDetailDto.setTotalInterestRateAmount(interestAmount);
            loanChargesApiRespDetailDto.setOtherCharges(otherCharges);
            loanChargesApiRespDetailDto.setNetLoanAmount(netLoan);
            loanChargesApiRespDetailDto.setTotalAmountToPay(totalAmountToPay);
            loanChargesApiRespDetailDto.setTenure(term.intValue());
            loanChargesApiRespDetailDto.setEligibleAmount(eligibleAmount);
            loanChargesApiRespDetailDto.setMonthlyReturnAmount(monthlyReturnAmount);
        }


        //*SCENARIO 5:*
//            Akiweka tenure na deduction=(desirable deduction*desirable tenure)/factor ya hiyo desirable tenure
        else if (requestedAmount.compareTo(BigDecimal.ZERO) == 0
                && desiredDeductibleAmount.compareTo(BigDecimal.ZERO) > 0
                && tenure > 2) {

            Price priceDetail = priceClientRvService.findWithXLoanTermAndDescription(description, tenure);
            BigDecimal factor = BigDecimal.valueOf(priceDetail.getFactor());
            BigDecimal term = BigDecimal.valueOf(priceDetail.getLoanTerm());


            //adminFee
            BigDecimal adminFee = BigDecimal
                    .valueOf(priceDetail.getAdminPercentage());

            //insurance
            BigDecimal insurance = BigDecimal
                    .valueOf(priceDetail.getInsurancePercentage());

            //deductibleAmount
            BigDecimal deductibleAmount = desiredDeductibleAmount;

            //requestAmount
            requestedAmount = desiredDeductibleAmount.multiply(term)
                    .divide(factor, 2, RoundingMode.HALF_UP);

            //insuranceAmount
            BigDecimal insuranceAmount =
                    deductibleAmount
                            .divide(insurance.add(BigDecimal.ONE), 2, RoundingMode.HALF_UP)
                            .multiply(insurance)
                            .multiply(term)
                            .setScale(2, RoundingMode.HALF_UP);

            //NetLoanAmount = requestedAmount
            BigDecimal netLoan = requestedAmount;

            //Processing Fees
            BigDecimal processingFee = requestedAmount
                    .multiply(adminFee.add(BigDecimal.ONE))
                    .subtract(requestedAmount)
                    .setScale(2, RoundingMode.HALF_UP);


            //totalAmountToPay
            BigDecimal totalAmountToPay = deductibleAmount
                    .multiply(term)
                    .setScale(2, RoundingMode.HALF_UP);

            //Interest Amount
            BigDecimal interestAmount = totalAmountToPay
                    .subtract(netLoan)
                    .subtract(processingFee)
                    .subtract(insuranceAmount)
                    .setScale(2, RoundingMode.HALF_UP);

            //other charges
            BigDecimal otherCharges = ((requestedAmount
                    .multiply(adminFee.add(BigDecimal.ONE)))
                    .subtract(requestedAmount))
                    .multiply(VATPercent)
                    .setScale(2, RoundingMode.HALF_UP);

            //Eligible Amount
            BigDecimal eligibleAmount = BigDecimal.ZERO;

            //MonthlyReturnAmount
            BigDecimal monthlyReturnAmount = deductibleAmount;

            loanChargesApiRespDetailDto.setDesiredDeductibleAmount(deductibleAmount);
            loanChargesApiRespDetailDto.setDesiredDeductibleAmount(deductibleAmount);
            loanChargesApiRespDetailDto.setTotalInsurance(insuranceAmount);
            loanChargesApiRespDetailDto.setTotalProcessingFee(processingFee);
            loanChargesApiRespDetailDto.setTotalInterestRateAmount(interestAmount);
            loanChargesApiRespDetailDto.setOtherCharges(otherCharges);
            loanChargesApiRespDetailDto.setNetLoanAmount(netLoan);
            loanChargesApiRespDetailDto.setTotalAmountToPay(totalAmountToPay);
            loanChargesApiRespDetailDto.setTenure(term.intValue());
            loanChargesApiRespDetailDto.setEligibleAmount(eligibleAmount);
            loanChargesApiRespDetailDto.setMonthlyReturnAmount(monthlyReturnAmount);
        }

        //*SCENARIO 6:*
        //Akiweka tenure pekee=(max installment*desirable tenure)/factor ya hiyo desirable tenure
        else if (requestedAmount.compareTo(BigDecimal.ZERO) == 0
                && desiredDeductibleAmount.compareTo(BigDecimal.ZERO) == 0
                && tenure > 2) {

            Price priceDetail = priceClientRvService.findWithXLoanTermAndDescription(description, tenure);
            BigDecimal factor = BigDecimal.valueOf(priceDetail.getFactor());
            BigDecimal term = BigDecimal.valueOf(priceDetail.getLoanTerm());

            //adminFee
            BigDecimal adminFee = BigDecimal
                    .valueOf(priceDetail.getAdminPercentage());

            //insurance
            BigDecimal insurance = BigDecimal
                    .valueOf(priceDetail.getInsurancePercentage());

            //requestAmount
            requestedAmount = deductableAmount.multiply(BigDecimal.valueOf(tenure))
                    .divide(factor, 2, RoundingMode.HALF_UP);

            //deductibleAmount
            BigDecimal deductibleAmount = deductableAmount;

            //insuranceAmount
            BigDecimal insuranceAmount =
                    deductibleAmount
                            .divide(insurance.add(BigDecimal.ONE), 2, RoundingMode.HALF_UP)
                            .multiply(insurance)
                            .multiply(term)
                            .setScale(2, RoundingMode.HALF_UP);

            //NetLoanAmount = requestedAmount
            BigDecimal netLoan = requestedAmount;

            //Processing Fees
            BigDecimal processingFee = requestedAmount
                    .multiply(adminFee.add(BigDecimal.ONE))
                    .subtract(requestedAmount)
                    .setScale(2, RoundingMode.HALF_UP);


            //totalAmountToPay
            BigDecimal totalAmountToPay = deductibleAmount
                    .multiply(term)
                    .setScale(2, RoundingMode.HALF_UP);

            //Interest Amount
            BigDecimal interestAmount = totalAmountToPay
                    .subtract(netLoan)
                    .subtract(processingFee)
                    .subtract(insuranceAmount)
                    .setScale(2, RoundingMode.HALF_UP);

            //other charges
            BigDecimal otherCharges = ((requestedAmount
                    .multiply(adminFee.add(BigDecimal.ONE)))
                    .subtract(requestedAmount))
                    .multiply(VATPercent)
                    .setScale(2, RoundingMode.HALF_UP);

            //Eligible Amount
            BigDecimal eligibleAmount = BigDecimal.ZERO;

            //MonthlyReturnAmount
            BigDecimal monthlyReturnAmount = deductibleAmount;

            loanChargesApiRespDetailDto.setDesiredDeductibleAmount(deductibleAmount);
            loanChargesApiRespDetailDto.setDesiredDeductibleAmount(deductibleAmount);
            loanChargesApiRespDetailDto.setTotalInsurance(insuranceAmount);
            loanChargesApiRespDetailDto.setTotalProcessingFee(processingFee);
            loanChargesApiRespDetailDto.setTotalInterestRateAmount(interestAmount);
            loanChargesApiRespDetailDto.setOtherCharges(otherCharges);
            loanChargesApiRespDetailDto.setNetLoanAmount(netLoan);
            loanChargesApiRespDetailDto.setTotalAmountToPay(totalAmountToPay);
            loanChargesApiRespDetailDto.setTenure(term.intValue());
            loanChargesApiRespDetailDto.setEligibleAmount(eligibleAmount);
            loanChargesApiRespDetailDto.setMonthlyReturnAmount(monthlyReturnAmount);
        }

        //*SCENARIO 7:*
        //Akiweka zote (loan Amount,deduction,tenure)=(desirable loan amount*factor ya hiyo desirable tenure) /desirable tenure (ignore desirable deduction alioweka)
        else if (requestedAmount.compareTo(BigDecimal.ZERO) > 0
                && desiredDeductibleAmount.compareTo(BigDecimal.ZERO) > 0
                && tenure > 2) {

            Price priceDetail = priceClientRvService.findWithXLoanTermAndDescription(description, tenure);
            BigDecimal factor = BigDecimal.valueOf(priceDetail.getFactor());
            BigDecimal term = BigDecimal.valueOf(priceDetail.getLoanTerm());

            //adminFee
            BigDecimal adminFee = BigDecimal
                    .valueOf(priceDetail.getAdminPercentage());

            //insurance
            BigDecimal insurance = BigDecimal
                    .valueOf(priceDetail.getInsurancePercentage());

            //deductibleAmount
            BigDecimal deductibleAmount = requestedAmount.multiply(factor)
                    .divide(BigDecimal.valueOf(tenure), 2, RoundingMode.HALF_UP);

            //insuranceAmount
            BigDecimal insuranceAmount =
                    deductibleAmount
                            .divide(insurance.add(BigDecimal.ONE), 2, RoundingMode.HALF_UP)
                            .multiply(insurance)
                            .multiply(term)
                            .setScale(2, RoundingMode.HALF_UP);

            //NetLoanAmount = requestedAmount
            BigDecimal netLoan = requestedAmount;

            //Processing Fees
            BigDecimal processingFee = requestedAmount
                    .multiply(adminFee.add(BigDecimal.ONE))
                    .subtract(requestedAmount)
                    .setScale(2, RoundingMode.HALF_UP);


            //totalAmountToPay
            BigDecimal totalAmountToPay = deductibleAmount
                    .multiply(term)
                    .setScale(2, RoundingMode.HALF_UP);

            //Interest Amount
            BigDecimal interestAmount = totalAmountToPay
                    .subtract(netLoan)
                    .subtract(processingFee)
                    .subtract(insuranceAmount)
                    .setScale(2, RoundingMode.HALF_UP);

            //other charges
            BigDecimal otherCharges = ((requestedAmount
                    .multiply(adminFee.add(BigDecimal.ONE)))
                    .subtract(requestedAmount))
                    .multiply(VATPercent)
                    .setScale(2, RoundingMode.HALF_UP);

            //Eligible Amount
            BigDecimal eligibleAmount = BigDecimal.ZERO;

            //MonthlyReturnAmount
            BigDecimal monthlyReturnAmount = deductibleAmount;

            loanChargesApiRespDetailDto.setDesiredDeductibleAmount(deductibleAmount);
            loanChargesApiRespDetailDto.setDesiredDeductibleAmount(deductibleAmount);
            loanChargesApiRespDetailDto.setTotalInsurance(insuranceAmount);
            loanChargesApiRespDetailDto.setTotalProcessingFee(processingFee);
            loanChargesApiRespDetailDto.setTotalInterestRateAmount(interestAmount);
            loanChargesApiRespDetailDto.setOtherCharges(otherCharges);
            loanChargesApiRespDetailDto.setNetLoanAmount(netLoan);
            loanChargesApiRespDetailDto.setTotalAmountToPay(totalAmountToPay);
            loanChargesApiRespDetailDto.setTenure(term.intValue());
            loanChargesApiRespDetailDto.setEligibleAmount(eligibleAmount);
            loanChargesApiRespDetailDto.setMonthlyReturnAmount(monthlyReturnAmount);

        }
        return loanChargesApiRespDetailDto;

    }
}

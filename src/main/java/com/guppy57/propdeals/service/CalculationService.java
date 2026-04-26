package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.QueueMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    private static final Logger log = LoggerFactory.getLogger(CalculationService.class);

    public void processNewAnalysis(QueueMessage message) {
        log.info("processNewAnalysis called: analysisId={}", message.analysisId());
        // TODO: implement LTR / flip calculation for new analysis
    }

    public void processReanalysis(QueueMessage message) {
        log.info("processReanalysis called: analysisId={}", message.analysisId());
        // TODO: implement reprocessing logic
    }

    public void applyPrimaryCalculations() {}

    public void applyClosingCostCalculations() {
        // TODO - use values from Assumption Set / User Settings if possible
    }

    public void applyInvestmentCalculations() {}

    public double calculateMortgage() { return 0; }

    public double calculateIRR() { return 0; }

    public double calculateNPV() { return 0; }

    public double calculatePaybackPeriod() { return 0; }

    public double calculateNetProceeds() { return 0; }

    public double calculateExpectedGains() { return 0; }

    public double calculateEmergencyFund() { return 0; }
}

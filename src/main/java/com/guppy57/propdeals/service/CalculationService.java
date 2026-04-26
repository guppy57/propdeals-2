package com.guppy57.propdeals.service;

import com.guppy57.propdeals.dto.QueueMessage;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    public void processNewAnalysis(QueueMessage message) {
        // TODO: implement LTR / flip calculation for new analysis
        // get the assumption set
        // get the loan
        // get the property
        // get the property's units
        // (get the user's settings)
        // determine which unit belongs to the user IF it is a HOUSE HACK
        // TODO - this will eventually be changed to be based on the user's setting
    }

    public void processReanalysis(QueueMessage message) {
        // TODO: implement reprocessing logic
    }

    public void applyPrimaryCalculations() {}

    public void applyClosingCostCalculations() {
        // TODO - use values from Assumption Set / User Settings if possible
        // TODO - default to something if the user doesn't have anything set
    }

    public void applyInvestmentCalculations() {}

    public double calculateMortgage() {}

    public double calculateIRR() {}

    public double calculateNPV() {}

    public double calculatePaybackPeriod() {}

    public double calculateNetProceeds() {}

    public double calculateExpectedGains() {}

    public double calculateEmergencyFund() {
        // TODO - figure out how to make this something anyone can use? Might have to be a settings option where you can put in:
        // TODO - number of months, and a per-month fund amount and then we add in on the backend PITI, etc.
    }
}

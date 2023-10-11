package org.whitneyrobotics.ftc.teamcode.Constants;

public enum Alliance {
    RED(1),BLUE(-1);
    public final int allianceCoefficient;
    Alliance(int allianceCoefficient){
        this.allianceCoefficient = allianceCoefficient;
    }
}

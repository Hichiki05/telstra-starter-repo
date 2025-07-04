package au.com.telstra.simcardactivator;

public class ActivationRequestToActuator {
    private String iccid;

    public ActivationRequestToActuator() {}

    public ActivationRequestToActuator(String iccid) {
        this.iccid = iccid;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }
}

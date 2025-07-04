package au.com.telstra.simcardactivator;

public class ActivationResult {
    private boolean success;

    public ActivationResult() {}

    public ActivationResult(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}


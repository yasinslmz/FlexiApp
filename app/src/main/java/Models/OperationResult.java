package Models;

public class OperationResult {
    private Integer id;
    private String OperationMessage;
    private String DetailMessage;
    private OperationStatus OperationStatus;

    // Getter ve Setter metotları
    public Integer getID() {
        return id;
    }

    public void setID(Integer ID) {
        this.id = ID;
    }

    public String getOperationMessage() {
        return OperationMessage;
    }

    public void setOperationMessage(String operationMessage) {
        OperationMessage = operationMessage;
    }

    public String getDetailMessage() {
        return DetailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        DetailMessage = detailMessage;
    }

    public OperationStatus getOperationStatus() {
        return OperationStatus;
    }

    public void setOperationStatus(OperationStatus operationStatus) {
        OperationStatus = operationStatus;
    }

    // OperationStatus enum
    public enum OperationStatus {
        Success,
        Exception,
        Error,
        DublicateRecord,
        Other,
        Warning,
        Bypass
    }
}

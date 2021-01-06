package tz.go.moh.him.hfr.mediator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * Represents an EPICOR request.
 */
public class EpicorHfrRequest extends HfrRequest {

    /**
     * The transaction id number from the IL.
     */
    @JsonProperty("IL_IDNumber")
    @SerializedName("IL_IDNumber")
    private String transactionIdNumber;

    /**
     * Initializes a new instance of the {@link EpicorHfrRequest} class.
     */
    public EpicorHfrRequest() {
    }

    /**
     * Initializes a new instance of the {@link EpicorHfrRequest} class.
     * @param transactionIdNumber The transaction id number.
     */
    public EpicorHfrRequest(String transactionIdNumber) {
        this.transactionIdNumber = transactionIdNumber;
    }

    /**
     * Initializes a new instance of the {@link EpicorHfrRequest} class.
     * @param request The existing {@link HfrRequest} instance.
     */
    public EpicorHfrRequest(HfrRequest request) {
        super(request);
    }

    /**
     * Gets the transaction id number.
     * @return Returns the transaction id number.
     */
    public String getTransactionIdNumber() {
        return transactionIdNumber;
    }

    /**
     * Sets the transaction id number.
     * @param transactionIdNumber The transaction id number.
     */
    public void setTransactionIdNumber(String transactionIdNumber) {
        this.transactionIdNumber = transactionIdNumber;
    }
}

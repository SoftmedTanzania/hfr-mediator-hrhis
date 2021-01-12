package tz.go.moh.him.hfr.mediator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import tz.go.moh.him.mediator.core.exceptions.ArgumentNullException;
import tz.go.moh.him.mediator.core.utils.StringUtils;

/**
 * Represents an acknowledgement.
 */
public class Ack {

    /**
     * The transaction id number.
     */
    @JsonProperty("IL_IDNumber")
    @SerializedName("IL_IDNumber")
    private String transactionIdNumber;

    /**
     * The status.
     */
    @JsonProperty("status")
    @SerializedName("status")
    private String status;

    /**
     * Initializes a new instance of the {@link Ack} class.
     */
    public Ack() {
    }

    /**
     * Initializes a new instance of the {@link Ack} class.
     *
     * @param transactionIdNumber The transaction id number.
     * @param status              The status.
     */
    public Ack(String transactionIdNumber, String status) {

        if (StringUtils.isNullOrEmpty(transactionIdNumber)) {
            throw new ArgumentNullException("transactionIdNumber - Value cannot be null");
        }

        if (StringUtils.isNullOrEmpty(status)) {
            throw new ArgumentNullException("status - Value cannot be null");
        }

        this.setTransactionIdNumber(transactionIdNumber);
        this.setStatus(status);
    }

    /**
     * Gets the transaction id number.
     *
     * @return Returns the transaction id number.
     */
    public String getTransactionIdNumber() {
        return transactionIdNumber;
    }

    /**
     * Sets the transaction id number.
     *
     * @param transactionIdNumber The transaction id number.
     */
    public void setTransactionIdNumber(String transactionIdNumber) {
        this.transactionIdNumber = transactionIdNumber;
    }

    /**
     * Gets the status.
     *
     * @return Returns the status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status The status.
     */
    public void setStatus(String status) {
        this.status = status;
    }
}

package models;


import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by renatosierra on 7/23/14.
 */
@Entity
public class Transaction extends Model {
    Date date;
    @Column(nullable=true)
    String request;
    @Column(nullable=true)
    String response;
    @Column(nullable=true)
    long responseTime;
    @Column(nullable=true)
    int status;
    @Required
    @ManyToOne
    @JoinColumn(nullable=true, name="ErrorType_id")
    ErrorType errorType;
    @Required
    @ManyToOne
    @JoinColumn(nullable=true, name = "TransactionType_id")
    TransactionType transactionType;
    @ManyToOne
    @JoinColumn(nullable=true, name="User_id")
    User user;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device deviceId) {
        this.device = deviceId;
    }

    @ManyToOne
    @JoinColumn(nullable=true, name="Device_id")
    Device device;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

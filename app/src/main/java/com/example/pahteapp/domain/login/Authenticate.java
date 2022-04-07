package com.example.pahteapp.domain.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Authenticate {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("status_code")
    @Expose
    private Integer status_code;
    @SerializedName("expires_at")
    @Expose
    private String expiresAt;
    @SerializedName("request_token")
    @Expose
    private String requestToken;
    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("guest_session_id")
    @Expose
    private String guestSessionId;
    @SerializedName("status_message")
    @Expose
    private String statusMessage;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getGuestSessionId() {
        return guestSessionId;
    }

    public void setGuestSessionId(String guestSessionId) {
        this.guestSessionId = guestSessionId;
    }

    @Override
    public String toString() {
        return "Authenticate{" +
                "success=" + success +
                ", expiresAt='" + expiresAt + '\'' +
                ", requestToken='" + requestToken + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", guestSessionId='" + guestSessionId + '\'' +
                '}';
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Integer getStatus_code() {
        return status_code;
    }

    public void setStatus_code(Integer status_code) {
        this.status_code = status_code;
    }
}

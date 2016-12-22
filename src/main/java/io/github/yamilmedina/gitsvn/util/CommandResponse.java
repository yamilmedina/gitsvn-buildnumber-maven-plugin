package io.github.yamilmedina.gitsvn.util;

/**
 *
 * @author Y.Medina
 */
public class CommandResponse {

    public static final int SUCCESS_CODE = 0;

    private int responseCode = 1;
    private String response = "";

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean successfulExecution() {
        return this.responseCode == SUCCESS_CODE;
    }

}

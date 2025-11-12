package exception;

import com.google.gson.Gson;
import responses.ErrorResponse;

public class ResponseException extends Exception {

    final private int code;

    public ResponseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public String toJson() {
        return new Gson().toJson(new ErrorResponse(getMessage(), code));
    }

    public static ResponseException fromJson(String json) {
        var response = new Gson().fromJson(json, ErrorResponse.class);
        var status = response.status();
        String message = response.message();
        return new ResponseException(status, message);
    }

    public int code() {
        return code;
    }

}

























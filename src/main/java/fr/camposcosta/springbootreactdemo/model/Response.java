package fr.camposcosta.springbootreactdemo.model;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class Response<T> {
    /** timestamp of the request processing */
    private final LocalDateTime timestamp;

    /** response status code */
    private final int statusCode;

    /** response status */
    private final String statusMessage;

    /** error message, exclusive with data */
    private final String errorMessage;

    /**
     * output data, exclusive with errorMessage
     */
    private final Map<String, T> data;

    /**
     * Response OK constructor
     * 
     * @param timestamp timestamp
     * @param status    http status
     * @param data      data
     */
    public Response(LocalDateTime timestamp, HttpStatus status, Map<String, T> data) {
        this.timestamp = timestamp;
        this.statusCode = status.value();
        this.statusMessage = status.getReasonPhrase();
        this.errorMessage = null;
        this.data = data;
    }

    /**
     * Response NG constructor
     * 
     * @param timestamp    timestamp
     * @param status       status
     * @param errorMessage error message
     */
    public Response(LocalDateTime timestamp, HttpStatus status, String errorMessage) {
        this.timestamp = timestamp;
        this.statusCode = status.value();
        this.statusMessage = status.getReasonPhrase();
        this.errorMessage = errorMessage;
        this.data = null;
    }

    /**
     * No content Response
     * 
     * @param timestamp timestamp
     * @param status    status
     */
    public Response(LocalDateTime timestamp, HttpStatus status) {
        this.timestamp = timestamp;
        this.statusCode = status.value();
        this.statusMessage = status.getReasonPhrase();
        this.errorMessage = null;
        this.data = null;
    }
}

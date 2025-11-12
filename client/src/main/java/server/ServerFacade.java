package server;

import com.google.gson.Gson;

import exception.*;
import requests.*;
import responses.*;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResponse register(RegisterRequest request) throws ResponseException {
        var req = buildRequest("POST", "/user", request, null);
        var response = sendRequest(req);
        return handleResponse(response, RegisterResponse.class);
    }

    public LoginResponse login(LoginRequest request) throws ResponseException {
        var req = buildRequest("POST", "/session", request, null);
        var response = sendRequest(req);
        return handleResponse(response, LoginResponse.class);
    }

    public LogoutResponse logout(LogoutRequest request) throws ResponseException {
        var req = buildRequest("DELETE", "/session", null, request.authToken());
        var response = sendRequest(req);
        return handleResponse(response, LogoutResponse.class);
    }

    public void clear() throws ResponseException {
        var req = buildRequest("DELETE", "/db", null, null);
        sendRequest(req);
    }

    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            request.setHeader("authorization", authToken);
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (status != 200) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }
            throw new ResponseException(status, "other failure: " + status);
        }
        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }

}





























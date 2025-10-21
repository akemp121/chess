package handlers;

import dataaccess.*;
import io.javalin.http.*;
import com.google.gson.Gson;
import requests.*;
import responses.*;
import services.Service;

public class Handler {

    private final Service service = new Service();

    public void registerHandler(Context ctx) {
        Gson serializer = new Gson();
        RegisterRequest request = serializer.fromJson(ctx.body(), RegisterRequest.class);
        try {
            RegisterResponse response = service.register(request);
            ctx.json(serializer.toJson(response));
            ctx.status(200);
        } catch (AlreadyTakenException e) {
            ctx.json(serializer.toJson(new ErrorResponse(e.getMessage())));
            ctx.status(403);
        }
    }

    public void loginHandler(Context ctx) {
        Gson serializer = new Gson();
        LoginRequest request = serializer.fromJson(ctx.body(), LoginRequest.class);
        try {
            LoginResponse response = service.login(request);
            ctx.json(serializer.toJson(response));
            ctx.status(200);
        } catch (UnauthorizedException e) {
            ctx.json(serializer.toJson(new ErrorResponse(e.getMessage())));
            ctx.status(401);
        } catch (BadRequest e) {
            ctx.json(serializer.toJson(new ErrorResponse(e.getMessage())));
            ctx.status(400);
        }
    }

    public void logoutHandler(Context ctx) {
        Gson serializer = new Gson();
        LogoutRequest request = new LogoutRequest(ctx.header("authorization"));
        try {
            LogoutResponse response = service.logout(request);
            ctx.json(serializer.toJson(response));
            ctx.status(200);
        } catch (UnauthorizedException e) {
            ctx.json(serializer.toJson(new ErrorResponse(e.getMessage())));
            ctx.status(401);
        }
    }
}

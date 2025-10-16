package handlers;

import io.javalin.http.*;
import com.google.gson.Gson;
import requests.*;

public class Handler {

    public void registerHandler(Context ctx) {
        Gson serializer = new Gson();
        RegisterRequest request = serializer.fromJson(ctx.body(), RegisterRequest.class);
        ctx.json(serializer.toJson("q'em ha' naxye: " + request.username()));
        ctx.status(2112);
    }

}

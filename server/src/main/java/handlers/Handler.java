package handlers;

import io.javalin.http.*;
import com.google.gson.Gson;

public class Handler {

    public void registerHandler(Context ctx) {
        Gson serializer = new Gson();

        ctx.json(serializer.toJson("q'em ha' na'aatinak"));

        ctx.status(444);
    }

}

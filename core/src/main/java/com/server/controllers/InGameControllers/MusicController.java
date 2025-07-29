package com.server.controllers.InGameControllers;

import com.google.gson.JsonObject;
import com.server.utilities.Response;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class MusicController extends Controller {
    public AtomicReference<String> userWaitingForMusic = new AtomicReference<>("");

    public void uploadMusicHandler(Context ctx) {
        try {
            UploadedFile file = ctx.uploadedFile("file");
            if (file != null) {
                Path target;

                if (System.getProperty("user.dir").contains("core")) {
                    target = Paths.get("uploads", file.filename());
                } else {
                    target = Paths.get("core", "uploads", file.filename());
                }

                Files.createDirectories(target.getParent());
                try (InputStream is = file.content()) {
                    Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
                }
                ctx.json(Response.OK.setMessage("Successfully received music file"));

                System.out.println("Successfully received music file");
            } else {
                ctx.json(Response.BAD_REQUEST.setMessage("Failed to receive music file"));

                System.out.println("Failed to receive music file");
            }
        } catch (Exception e) {
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
            e.printStackTrace();
        }
    }

    public void downloadMusicHandler(Context ctx) {
        try {
            String filename = ctx.pathParam("filename");
            Path target;

            if (System.getProperty("user.dir").contains("core")) {
                target = Paths.get("uploads", filename);
            } else {
                target = Paths.get("core", "uploads", filename);
            }

            if (Files.exists(target)) {
                ctx.contentType(Files.probeContentType(target));
                ctx.header("Content-Disposition", "attachment; filename=\"" + filename + "\"");
                ctx.result(Files.newInputStream(target));

                System.out.println("Successfully sent music file.");
            } else {
                ctx.json(Response.BAD_REQUEST.setMessage("Failed to upload music file"));

                System.out.println("Failed to upload music file");
            }
        } catch (Exception e) {
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
            e.printStackTrace();
        }
    }

    public void handleMusicSyncing(Context ctx) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String senderUsername = (String) body.get("senderUsername");
            String syncTargetUsername = (String) body.get("syncTargetUsername");

            userWaitingForMusic.set(senderUsername);

            var req = new HashMap<String, String>();
            req.put("type", "MUSIC_QUERY");

            getGs().narrowCast(syncTargetUsername, req);
            ctx.json(Response.OK.setMessage("Please wait..."));
        } catch (Exception e) {
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
            e.printStackTrace();
        }
    }

    public void receiveMusicData(Context ctx) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            Boolean isPlaying = (Boolean) body.get("isPlaying");
            String musicName = (String) body.get("musicName");
            Double musicPosition = (Double) body.get("musicPos");

            HashMap<String, String> req = new HashMap<>();
            req.put("type", "SYNC_DATA");

            JsonObject data = new JsonObject();
            data.addProperty("musicName", musicName);
            data.addProperty("musicPos", musicPosition);
            data.addProperty("isPlaying", isPlaying);

            req.put("syncData", data.toString());
            getGs().narrowCast(userWaitingForMusic.get(), req);

            ctx.json(Response.OK.setMessage("Successfully received music data"));
        } catch (Exception e) {
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
            e.printStackTrace();
        }
    }
}

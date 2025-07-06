package com.server.controllers;

import com.common.models.User;
import com.common.models.networking.Lobby;
import com.server.repositories.LobbyRepository;
import com.server.repositories.UserRepository;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.HashMap;

public class LobbyController {
    public void createLobby(Context ctx) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String name = (String) body.get("name");
            Boolean isVisible = (Boolean) body.get("isVisible");
            Boolean isPublic = (Boolean) body.get("isPublic");
            String password = (String) body.get("password");

            String id = (String) ctx.attributeMap().get("id");

            User user = UserRepository.findUserById(id);
            if (user == null) {
                ctx.json(Response.UN_AUTHORIZED);
                return;
            }
            Lobby newLobby = new Lobby(isVisible, isPublic, name, user.getUsername());
            newLobby.getUsers().add(user.getUsername());
            if(!newLobby.isPublic()){
                if(password == null){
                    ctx.json(Response.BAD_REQUEST.setMessage("Password cannot be null"));
                    return;
                }
                newLobby.setPassword(password);
            }

            LobbyRepository.save(newLobby);
            user.setCurrentLobbyId(newLobby.get_id().toString());
            UserRepository.saveUser(user);
            ctx.json(Response.OK.setMessage("Lobby has been created successfully").setBody(newLobby));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }


    public void getLobbyById(Context ctx) {
        try{
            String id = ctx.pathParam("id");
            Lobby lobby = LobbyRepository.findById(id);

            if(lobby == null) {
                ctx.json(Response.NOT_FOUND.setMessage("Lobby with id " + id + " not found"));
                return;
            }

            ctx.json(Response.OK.setBody(lobby));
        }catch (Exception e){
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void getAllLobbies(Context ctx) {
        try{
            ArrayList<Lobby> lobbies = LobbyRepository.findAll(false);
            ctx.json(Response.OK.setBody(lobbies));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void getLobbiesByOwnerUsername(Context ctx) {
        try{
            ArrayList<Lobby> lobbies = LobbyRepository.findByOwnerUsername(ctx.pathParam("ownerUsername"));
            ctx.json(Response.OK.setBody(lobbies));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void getLobbyByName(Context ctx) {
        try{
            String name = ctx.queryParam("name");
            Lobby lobby = LobbyRepository.findByName(name);

            if(lobby == null) {
                ctx.json(Response.NOT_FOUND.setMessage("Lobby with name " + name + " not found"));
                return;
            }

            if(!lobby.isVisible()){
                ctx.json(Response.BAD_REQUEST.setMessage("Lobby with name " + name + " is not visible"));
                return;
            }

            if(!lobby.isPublic()){
                ctx.json(Response.BAD_REQUEST.setMessage("Lobby with name " + name + " is not public"));
                return;
            }

            ctx.json(Response.OK.setBody(lobby));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }
}

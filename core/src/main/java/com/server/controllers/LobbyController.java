package com.server.controllers;

import com.common.models.User;
import com.common.models.networking.Lobby;
import com.google.gson.Gson;
import com.server.GameServers.AppWebSocket;
import com.server.repositories.LobbyRepository;
import com.server.repositories.UserRepository;
import com.server.utilities.Response;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class LobbyController {
    public void joinLobby(Context ctx) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String id = ctx.attribute("id");
            String lobbyId = ctx.pathParam("id");
            String password = (String) body.get("passwrod");

            User user = UserRepository.findUserById(id);
            if (user == null) {
                ctx.json(Response.NOT_FOUND.setMessage("User not found"));
                return;
            }
            if (user.getCurrentLobbyId() != null) {
                ctx.json(Response.NOT_FOUND.setMessage("You are already in a lobby"));
                return;
            }
            Lobby lobby = LobbyRepository.findById(lobbyId);
            if (lobby == null) {
                ctx.json(Response.NOT_FOUND.setMessage("Lobby not found"));
                return;
            }
            if (lobby.getUsers().size() >= 4) {
                ctx.json(Response.BAD_REQUEST.setMessage("Too many users"));
                return;
            }
            if (lobby.getUsers().contains(user.getUsername())) {
                ctx.json(Response.BAD_REQUEST.setMessage("You are already in a lobby"));
                return;
            }
            if (!lobby.isPublic()) {
                if (!lobby.getPassword().equals(password)) {
                    ctx.json(Response.BAD_REQUEST.setMessage("Wrong password"));
                    return;
                }
            }
            user.setCurrentLobbyId(lobbyId);
            lobby.getUsers().add(user.getUsername());
            UserRepository.saveUser(user);
            LobbyRepository.save(lobby);

            ctx.json(Response.OK.setMessage("Lobby successfully joined").setBody(lobby));

            HashMap<String, String> response = new HashMap<>();
            response.put("type", "LOBBY_JOINED");
            response.put("message", "User " + user.getUsername() + " joined lobby");
            response.put("lobby", new Gson().toJson(lobby));
            AppWebSocket.sendMessageToLobby(lobby, user, new Gson().toJson(response));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void chooseFarm(Context ctx) {
        try {
            HashMap<String, Object> body = ctx.bodyAsClass(HashMap.class);
            String id = ctx.attribute("id");
            String farmName = ((String) body.get("farm"));
            int farm = farmName.equals("Farm 1") ? 1 : 2;

            User user = UserRepository.findUserById(id);
            if (user == null) {
                ctx.json(Response.NOT_FOUND.setMessage("User not found"));
                return;
            }

            Lobby lobby = LobbyRepository.findById(user.getCurrentLobbyId());
            lobby.getUsersFarm().put(user.getUsername(), farm);

            ctx.json(Response.OK.setMessage("Farm successfully chosen").setBody(lobby));
            HashMap<String, String> response = new HashMap<>();
            response.put("type", "FARM_CHOSEN");
            response.put("message", "User " + user.getUsername() + " choosed farm");
            response.put("lobby", new Gson().toJson(lobby));
            AppWebSocket.sendMessageToLobby(lobby, user, new Gson().toJson(response));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

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
            if (!newLobby.isPublic()) {
                if (password == null) {
                    ctx.json(Response.BAD_REQUEST.setMessage("Password cannot be null"));
                    return;
                }
                newLobby.setPassword(password);
            }

            LobbyRepository.save(newLobby);
            user.setCurrentLobbyId(newLobby.get_id().toString());
            UserRepository.saveUser(user);
            ctx.json(Response.OK.setMessage("Lobby has been created successfully").setBody(newLobby));
            (new Timer()).schedule(new TimerTask() {
                @Override
                public void run() {
                    if (newLobby.getUsers().size() == 1) {
                        User user = UserRepository.findUserByUsername(newLobby.getUsers().get(0));
                        user.setCurrentLobbyId(null);
                        LobbyRepository.delete(newLobby);
                        UserRepository.saveUser(user);
                        HashMap<String, String> response = new HashMap<>();
                        response.put("type", "LOBBY_REMOVED");
                        AppWebSocket.sendMessageToLobby(newLobby, user, new Gson().toJson(response));

                    }
                    this.cancel();
                }
            }, 5 * 60 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }


    public void getLobbyById(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            Lobby lobby = LobbyRepository.findById(id);

            if (lobby == null) {
                ctx.json(Response.NOT_FOUND.setMessage("Lobby with id " + id + " not found"));
                return;
            }

            ctx.json(Response.OK.setBody(lobby));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void getAllLobbies(Context ctx) {
        try {
            ArrayList<Lobby> lobbies = LobbyRepository.findAll(false);
            ctx.json(Response.OK.setBody(lobbies));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void getLobbiesByOwnerUsername(Context ctx) {
        try {
            ArrayList<Lobby> lobbies = LobbyRepository.findByOwnerUsername(ctx.pathParam("ownerUsername"));
            ctx.json(Response.OK.setBody(lobbies));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void getLobbyByName(Context ctx) {
        try {
            String name = ctx.queryParam("name");
            Lobby lobby = LobbyRepository.findByName(name);

            if (lobby == null) {
                ctx.json(Response.NOT_FOUND.setMessage("Lobby with name " + name + " not found"));
                return;
            }

            if (!lobby.isVisible()) {
                ctx.json(Response.BAD_REQUEST.setMessage("Lobby with name " + name + " is not visible"));
                return;
            }

            if (!lobby.isPublic()) {
                ctx.json(Response.BAD_REQUEST.setMessage("Lobby with name " + name + " is not public"));
                return;
            }

            ctx.json(Response.OK.setBody(lobby));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void getMyCurrentLobby(Context ctx) {
        try {
            String id = ctx.attribute("id");
            User user = UserRepository.findUserById(id);
            if (user == null) {
                ctx.json(Response.NOT_FOUND.setMessage("User with id " + id + " not found"));
                return;
            }
            Lobby lobby = LobbyRepository.findById(user.getCurrentLobbyId());
            ctx.json(Response.OK.setBody(lobby));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }

    public void leave(Context ctx) {
        try {
            String id = ctx.attribute("id");

            User user = UserRepository.findUserById(id);
            if (user == null) {
                ctx.json(Response.NOT_FOUND.setMessage("User with id " + id + " not found"));
                return;
            }
            Lobby lobby = LobbyRepository.findById(user.getCurrentLobbyId());
            if (lobby == null) {
                ctx.json(Response.NOT_FOUND.setMessage("You are not in a lobby"));
                return;
            }
            user.setCurrentLobbyId(null);
            lobby.getUsers().remove(user.getUsername());
            UserRepository.saveUser(user);
            if (lobby.getUsers().isEmpty()) {
                LobbyRepository.delete(lobby);
            } else
                LobbyRepository.save(lobby);

            ctx.json(Response.OK.setBody(lobby));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.json(Response.BAD_REQUEST.setMessage(e.getMessage()));
        }
    }
}

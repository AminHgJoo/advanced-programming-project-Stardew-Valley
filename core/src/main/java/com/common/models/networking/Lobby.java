package com.common.models.networking;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.HashMap;

@Entity
public class Lobby {
    @Id
    private String _id;
    private String name;
    private String password;
    private boolean isPublic;
    private boolean isVisible;
    private String ownerUsername;
    private ArrayList<String> users = new ArrayList<>();
    private HashMap<String, Integer> usersFarm = new HashMap<>();

    public Lobby() {
    }

    public Lobby(boolean isVisible, boolean isPublic, String name, String ownerUsername) {
        this.isVisible = isVisible;
        this.isPublic = isPublic;
        this.name = name;
        this.ownerUsername = ownerUsername;
        users.add(ownerUsername);
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Name: " + name + " | Public: " + isPublic + " | Owner: " + ownerUsername;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public HashMap<String, Integer> getUsersFarm() {
        return usersFarm;
    }

    public void setUsersFarm(HashMap<String, Integer> usersFarm) {
        this.usersFarm = usersFarm;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

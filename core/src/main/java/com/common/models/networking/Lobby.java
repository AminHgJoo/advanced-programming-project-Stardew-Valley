package com.common.models.networking;

import dev.morphia.annotations.Entity;

import java.util.ArrayList;

@Entity
public class Lobby {
    private String name;
    private String _id;
    private boolean isPublic;
    private boolean isVisible;
    private String ownerUsername;
    private ArrayList<String> users = new ArrayList<>();

    public Lobby() {}
    public Lobby(boolean isVisible, boolean isPublic, String name, String ownerNickname) {
        this.isVisible = isVisible;
        this.isPublic = isPublic;
        this.name = name;
        this.ownerUsername = ownerNickname;
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
        return "Name: " + name + " | ID: " + _id + " | Public: " + isPublic + " | Owner: " + ownerUsername;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }
}

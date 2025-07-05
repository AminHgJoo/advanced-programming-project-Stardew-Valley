package com.common.models.networking;

public class Lobby {
    private String name;
    private String id;
    private boolean isPublic;
    private boolean isVisible;
    private String ownerNickname;

    public Lobby(String id, boolean isVisible, boolean isPublic, String name, String ownerNickname) {
        this.id = id;
        this.isVisible = isVisible;
        this.isPublic = isPublic;
        this.name = name;
        this.ownerNickname = ownerNickname;
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

    public String getOwnerNickname() {
        return ownerNickname;
    }

    public void setOwnerNickname(String ownerNickname) {
        this.ownerNickname = ownerNickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Name: " + name + " | ID: " + id + " | Public: " + isPublic + " | Owner: " + ownerNickname;
    }
}

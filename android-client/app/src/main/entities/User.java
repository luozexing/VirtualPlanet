package com.virtualplanet.virtualplanet;

public class User {
    private int uid = null;
    private String username = null;
    private String coordinate = null;
    private static User instance = null;

    private User() {}

    public static User getInstance() {
        if (this.instance != null) {
            return this.instance;
        } else {
            return new this();
        }
    }

    public boolean signin() {}

    public boolean signup() {}

    public boolean signout() {}

    public boolean hasSignin() {}

    public int getUid() {
        return this.uid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getCoordinate() { return this.coordinate; }

    public setCoordinate(String coordinate) { this.coordinate = coordinate; }
}
package com.royken.teknik.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by royken on 05/04/18.
 */
public class ServerStatus implements Serializable{

    @Expose(serialize = true, deserialize = true)
    @SerializedName("server")
    private int server;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("status")
    private boolean status;

    public ServerStatus() {
    }

    public int getServer() {
        return server;
    }

    public void setServer(int server) {
        this.server = server;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

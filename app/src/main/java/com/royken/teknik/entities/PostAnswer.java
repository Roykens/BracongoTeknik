package com.royken.teknik.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by royken on 27/07/17.
 */
public class PostAnswer implements Serializable {

    private static final long serialVersionUID = -222864131214757023L;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("success")
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

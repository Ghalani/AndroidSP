package com.ghalani.ghalani.item.team;

import java.io.Serializable;

/**
 * Created by Amanze on 7/18/2016.
 */
public class Team implements Serializable {
    private String manager;
    private String id;
    private String name;

    public Team(String id, String name, String manager_id){
        this.id = id;
        this.name = name;
        this.manager = manager;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
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
}

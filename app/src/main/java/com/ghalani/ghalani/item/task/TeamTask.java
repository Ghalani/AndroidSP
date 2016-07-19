package com.ghalani.ghalani.item.task;


import java.io.Serializable;

/**
 * Created by Amanze on 7/19/2016.
 */
public class TeamTask implements Serializable {
    String id, name, description, comment, start, end;
    String farm;
    String fields;

    public TeamTask(String id, String name, String description, String comment,
                        String start, String end, String farm, String fields){
        this.id = id;
        this.name = name;
        this.description = description;
        this.comment = comment;
        this.start = start;
        this.end = end;
        this.farm = farm;
        this.fields = fields;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }
}

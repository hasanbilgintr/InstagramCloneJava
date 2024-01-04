package com.hasanbilgin.instagramclonejava.model;

public class PostModel {

    private String email;
    private String comment;
    private String dowsloadUrl;

    public PostModel(String email, String comment, String dowsloadUrl) {
        this.email = email;
        this.comment = comment;
        this.dowsloadUrl = dowsloadUrl;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDowsloadUrl() {
        return dowsloadUrl;
    }

    public void setDowsloadUrl(String dowsloadUrl) {
        this.dowsloadUrl = dowsloadUrl;
    }







}

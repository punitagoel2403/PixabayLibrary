package com.pixabay.pixabaylib.model;

public class ImagesModel {
    private int id;
    private String webformatURL;


    public ImagesModel(int id, String webformatURL) {
        this.id = id;
        this.webformatURL = webformatURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWebformatURL() {
        return webformatURL;
    }

    public void setWebformatURL(String webformatURL) {
        this.webformatURL = webformatURL;
    }
}

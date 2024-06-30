package com.pixabay.pixabaylib.controller;

import com.pixabay.pixabaylib.model.ImagesModel;

import java.util.ArrayList;

/**
 * This Interface is called once the imagesUrl are fetched from the api.
 */
public interface ImageDataActionFinished {
    void imageDataFetchFinished(ArrayList<ImagesModel> imagesModelArrayList);
}

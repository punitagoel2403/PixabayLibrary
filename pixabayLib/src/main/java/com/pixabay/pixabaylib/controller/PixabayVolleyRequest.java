package com.pixabay.pixabaylib.controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pixabay.pixabaylib.model.ImagesModel;
import com.pixabay.pixabaylib.util.UrlConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PixabayVolleyRequest {
    public final String TAG = getClass().getSimpleName();
    protected String url;
    private final Context context;
    private int noOfPages;
    private final String searchStr;

    public PixabayVolleyRequest(String searchStr, Context context) {
        this.context = context;
        this.searchStr = searchStr;
        this.url = UrlConstants.PIXABAY_API + searchStr + UrlConstants.PIXABAY_ATTRIBUTES;
    }

    /**
     * In getNoOfPages will GET the total no of pages for given user details query
     *
     * @param actionFinished called when data are fetched
     * @param url            used in jsonObjectRequest for the network call
     * @return
     */

    public int getNoOfPages(final PageDataActionFinished actionFinished, String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int totalHits = response.getInt("totalHits");
                    if (totalHits >= 20) {
                        noOfPages = (totalHits / 20);
                    } else if (totalHits < 20 && totalHits > 0) {
                        noOfPages = 1;
                    } else {
                        noOfPages = 0;
                    }
                } catch (JSONException e) {
                }
                if (null != actionFinished) {
                    actionFinished.fetchPageData(noOfPages);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        return noOfPages;
    }

    /**
     * This method is to fetch data from single page which is used in getAllPageImages method
     *
     * @param imageDataActionFinished called once the network call is finished
     * @param no                      gives the page no.
     */

    public void fetchSinglePageImages(final ImageDataActionFinished imageDataActionFinished, int no) {
        final ArrayList<ImagesModel> imagesModels = new ArrayList<>();
        String pageUrl = url.concat(String.valueOf(no));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, pageUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("hits");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ImagesModel imagesModel = new ImagesModel(jsonObject.getInt("id"),
                                jsonObject.getString("webformatURL"));
                        imagesModels.add(imagesModel);
                    }
                } catch (JSONException e) {
                }
                if (null != imageDataActionFinished) {
                    imageDataActionFinished.imageDataFetchFinished(imagesModels);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
    /**
     * In getAllPageImages method fetchSinglePage method is called
     * with increment of the page number synchronously
     * with the help of for loop
     * @param callBack once called when data is fetched
     * @param Page     is total no of pages

    public void getAllPageImages(final ImageDataActionFinished callBack, int Page) {
    for (int i = 1; i <= Page; i++) {
    fetchSinglePageImages(callBack::imageDataFetchFinished, i);
    }
    }
     */
}


package com.pixabay.pixabaylib.util;

public class UrlConstants {
    public static final String PIXABAY_URL = "https://pixabay.com/api/";
    public static final String KEY_AND_SEARCH = "?key={ KEY }&q="; // todo: generate pixabay key and replace { KEY } with the generated key
    public static final String PIXABAY_ATTRIBUTES = "&image_type=photo&safesearch=true&orientation=vertical&page=";
    public static final String PIXABAY_API = PIXABAY_URL + KEY_AND_SEARCH;
    public static final String PIXABAY_WEBSITE_URL = "https://pixabay.com/";
}

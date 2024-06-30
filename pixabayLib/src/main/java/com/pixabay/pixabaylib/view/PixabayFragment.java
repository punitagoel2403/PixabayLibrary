package com.pixabay.pixabaylib.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pixabay.pixabaylib.R;
import com.pixabay.pixabaylib.controller.ImageDataActionFinished;
import com.pixabay.pixabaylib.controller.PageDataActionFinished;
import com.pixabay.pixabaylib.controller.PixabayVolleyRequest;
import com.pixabay.pixabaylib.model.ImagesModel;
import com.pixabay.pixabaylib.util.UrlConstants;

import java.io.File;
import java.util.ArrayList;

/**
 * This is home fragment appears where the user can
 * search type of pictures needed also filter with category
 * and onClick of search button it will intent to image fragment
 * where all the related images will be displayed in gridView.
 */

public class PixabayFragment extends Fragment implements GridImagesAdapter.OnPreviewListener {
    public static final String TAG = "PixabayFragment";
    private EditText etSearchImage;
    private PixabayVolleyRequest pixabayVolleyRequest;
    private final Context mContext;
    private RecyclerView imgRecyclerView;
    private final ArrayList<ImagesModel> imagesModels = new ArrayList<>();
    private final GridImagesAdapter.OnPreviewListener onPreviewListener = this;
    private int totalPages;
    private GridLayoutManager gridLayoutManager;
    private int totalItemCount;
    private int fistVisibleItemCount;
    private int visibleItemCount;
    private boolean load = true;
    private int previousTotal;
    private int currentPage = 1;
    private GridImagesAdapter gridImagesAdapter;
    private GridSpacingItemDecoration gridSpacingItemDecoration;
    private final int visibleThreshold = 5;
    private RecyclerView.OnScrollListener scrollOnListener;
    private ProgressBar progressLoading;
    private final GetImageUrlClicked getImageUrlClicked;
    private ImageView imgBtnPixabayLogo;


    public PixabayFragment(Context context, GetImageUrlClicked urlClicked) {
        this.mContext = context;
        this.getImageUrlClicked = urlClicked;
    }

    /**
     * this onCreateView method initialize all the editText,Spinner views
     * And onClick on search Button intent to image fragment with the needed params
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.home_pixabay, container, false);
        etSearchImage = homeView.findViewById(R.id.et_search_pixabay);
        AppCompatImageButton searchBtn = homeView.findViewById(R.id.search_imgBtn_pixabay);
        imgRecyclerView = homeView.findViewById(R.id.images_grid_rv_pixabay);
        progressLoading = homeView.findViewById(R.id.progress_loading_pixabay);
        imgBtnPixabayLogo = homeView.findViewById(R.id.imageButton_pixabay_logo);
        progressLoading.setVisibility(View.INVISIBLE);

        getImages("background");
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PixabayFragment.this.getImages(etSearchImage.getText().toString());
            }
        });
        imgBtnPixabayLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(UrlConstants.PIXABAY_WEBSITE_URL));
                startActivity(intent);
            }
        });

        return homeView;
    }

    /**
     * This method will used to insert the imagesArrayList into the recyclerview
     * with gridLayoutManager decorated with GridSpacingItemDecoration
     * @param imagesModelArrayList
     */

    private void insertImagesToRV(ArrayList<ImagesModel> imagesModelArrayList) {
        imgRecyclerView.removeItemDecoration(gridSpacingItemDecoration);
        gridImagesAdapter = new GridImagesAdapter(mContext, imagesModelArrayList, onPreviewListener);
        gridLayoutManager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
        int spanCount = 3;
        int spacing = 30;
        imgRecyclerView.setLayoutManager(gridLayoutManager);
        imgRecyclerView.addItemDecoration(gridSpacingItemDecoration = new GridSpacingItemDecoration(spanCount, spacing, true));
        imgRecyclerView.setAdapter(gridImagesAdapter);
        gridImagesAdapter.notifyDataSetChanged();
        pagination();
    }

    private void getImages(String searchStr) {
        progressLoading.setVisibility(View.VISIBLE);
        imgRecyclerView.removeOnScrollListener(scrollOnListener);
        imagesModels.clear();
        currentPage = 1;
        previousTotal = 0;
        pixabayVolleyRequest = new PixabayVolleyRequest(searchStr, mContext);
        String urlPage = UrlConstants.PIXABAY_API + etSearchImage.getText().toString() + UrlConstants.PIXABAY_ATTRIBUTES;
        pixabayVolleyRequest.getNoOfPages(new PageDataActionFinished() {
            @Override
            public void fetchPageData(int totalPage) {
                totalPages = totalPage;
                PixabayFragment.this.loadPage();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressLoading.setVisibility(View.INVISIBLE);
                        insertImagesToRV(imagesModels);
                    }
                }, 3000);
            }
        }, urlPage);
    }

    private void pagination() {
        scrollOnListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = gridLayoutManager.getChildCount();
                totalItemCount = gridLayoutManager.getItemCount();
                fistVisibleItemCount = gridLayoutManager.findFirstVisibleItemPosition();
                if (load) {
                    if (totalItemCount > previousTotal) {
                        load = false;
                        previousTotal = totalItemCount;
                    }
                }

                if (!load && (totalItemCount - visibleItemCount) <= (fistVisibleItemCount + visibleThreshold)) {
                    if (currentPage < totalPages) {
                        currentPage++;
                    }
                    loadPage();
                    load = true;
                }
            }

        };
        imgRecyclerView.addOnScrollListener(scrollOnListener);
    }

    private void loadPage() {
        pixabayVolleyRequest.fetchSinglePageImages(new ImageDataActionFinished() {
            @Override
            public void imageDataFetchFinished(ArrayList<ImagesModel> imagesModelArrayList) {
                imagesModels.addAll(imagesModelArrayList);
            }
        }, currentPage);
    }

    @Override
    public void onPreviewImageClicked(String url) {
        getImageUrlClicked.imageClicked(url);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!imagesModels.isEmpty()) {
            insertImagesToRV(imagesModels);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        deleteCache(mContext);
    }

    public interface GetImageUrlClicked {
        void imageClicked(String url);
    }
}

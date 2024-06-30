package com.pixabay.pixabayproject.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixabay.pixabaylib.view.PixabayFragment;
import com.pixabay.pixabayproject.R;


public class DemoFragment extends Fragment implements PixabayFragment.GetImageUrlClicked {
    private Context context;
    public static final String TAG="DemoFragment";
    private PixabayFragment.GetImageUrlClicked getImageUrlClicked=this::imageClicked;
    public DemoFragment() {}

    public DemoFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_demo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        PixabayFragment pixabayChildFragment=new PixabayFragment(context,getImageUrlClicked);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_frag, pixabayChildFragment).commit();
    }

    @Override
    public void imageClicked(String url) {
        Log.d(TAG, "imageClicked: "+url);
    }
}
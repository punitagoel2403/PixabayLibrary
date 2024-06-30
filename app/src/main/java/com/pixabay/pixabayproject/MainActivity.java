package com.pixabay.pixabayproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.pixabay.pixabaylib.view.PixabayFragment;
import com.pixabay.pixabayproject.fragment.DemoFragment;

public class MainActivity extends AppCompatActivity implements PixabayFragment.GetImageUrlClicked {
    private static final String TAG = "MainActivity";
    private PixabayFragment.GetImageUrlClicked getImageUrlClicked=this::imageClicked;
private FragmentTransaction fragmentTransaction;
private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager=getSupportFragmentManager();
//        To call pixabayFragment in Activity
//        insertPixabayFragment();
//        To call pixabayFragment from the fragment(nested Fragment)
        insertDemoFragment();
    }

    /**
     * This pixabay library can be implemented with the pixabayFragment...
     *....So we can use it either in Activity by calling pixabayFragment..
     * as shown below
     */
    private void insertPixabayFragment(){
        PixabayFragment pixabayFragment=new PixabayFragment(this,getImageUrlClicked);
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_main,pixabayFragment);
        fragmentTransaction.commit();
    }

    /**
     * This pixabay library can be implemented with the pixabayFragment...
     * ...So if we want to implement in fragment we need insert pixabayFragment
     * inside the fragment needed to be attached
     * Below method shows that pixabayFragment is called inside Demo Fragment
     */

    private void insertDemoFragment(){
        DemoFragment demoFragment =new DemoFragment(this);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_main, demoFragment).commit();
    }

    @Override
    public void imageClicked(String url) {
        Log.d(TAG, "imageClicked: "+url);
    }
}
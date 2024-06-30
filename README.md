# pixabayLib
pixabay library access, and selection of its media files

PIXABAY LIBRARY
 This library will fetch the images from Pixabay API  accordance to related search query and load it in a recyclerview with type gridLayout and onCLick of particular
 image will give the url of the respective Image

To use Pixabay Library

Download pixabayLib-debug.aar add it in your android project by navigating to project structure
in your android project and add dependencies via jar dependency and copy the path of the .arr file

Include these below dependecies before inserting .arr file to project
  implementation 'com.android.volley:volley:1.1.1'
  implementation 'com.squareup.picasso:picasso:2.71828'
  implementation "androidx.recyclerview:recyclerview:1.2.0-beta01"
   
Also generate API key from Pixabay website and change KEY in UrlConstants

Refer pixabayLib/app/src/main/java/com/pixabay/pixabayproject/MainActivity.java to get know how to implement the pixabay library.

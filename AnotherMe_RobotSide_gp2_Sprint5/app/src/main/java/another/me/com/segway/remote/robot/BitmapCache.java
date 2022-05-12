package another.me.com.segway.remote.robot;



import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;


// class BitmapCache for images control
public class BitmapCache extends LruCache<String, Bitmap> implements
        ImageCache {
    // check the size of the images
    // max memory for image is divided by 1024 bites
    // cach size divides by 8 bites
    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        return cacheSize;
    }

    public BitmapCache() {
        this(getDefaultLruCacheSize());
    }

    // send the images bites by calling the super class
    public BitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    @Override
    // get the size by multiply the height and width of image then divide by 1024 bits
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}// end class bitmap caches
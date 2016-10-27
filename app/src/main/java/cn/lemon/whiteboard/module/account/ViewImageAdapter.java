package cn.lemon.whiteboard.module.account;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.alien95.util.ImageUtil;

class ViewImageAdapter extends PagerAdapter {

    private static final String TAG = "ViewImageAdapter";
    private Map<String,WeakReference<Bitmap>> mCacheBitmaps;
    private List<String> mFilePaths;
    private Context mContext;

    public ViewImageAdapter(List<String> data, Context context) {
        mFilePaths = data;
        mContext = context;
        mCacheBitmaps = new HashMap<>(5);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final String key = mFilePaths.get(position);
        final ImageView image = new ImageView(mContext);
        image.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        image.setAdjustViewBounds(true);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if(mCacheBitmaps.containsKey(key)){
            image.setImageBitmap(mCacheBitmaps.get(key).get());
        }else {
            ImageUtil.getBitmapFromPath(key, new ImageUtil.Callback() {
                @Override
                public void callback(Bitmap bitmap) {
                    image.setImageBitmap(bitmap);
                    mCacheBitmaps.put(key,new WeakReference<>(bitmap));
                }
            });
        }
        container.addView(image);
        return image;
    }

    @Override
    public int getCount() {
        return mFilePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
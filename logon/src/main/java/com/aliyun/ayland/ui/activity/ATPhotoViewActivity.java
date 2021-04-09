package com.aliyun.ayland.ui.activity;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.photoview.PhotoView;

import java.util.ArrayList;

public class ATPhotoViewActivity extends ATBaseActivity {
    private ATMyTitleBar titlebar;
    private ViewPager viewpager;
//    private Dialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_photo_preview;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        viewpager = findViewById(R.id.viewpager);
        init();
    }

    @Override
    protected void initPresenter() {
    }

    private void init() {
        ArrayList<String> imgList = getIntent().getStringArrayListExtra("data");
        int i = getIntent().getIntExtra("index", 0);
        titlebar.setTitle(i + 1 + "/" + imgList.size());

        viewpager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imgList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, final int position) {
                final PhotoView view = new PhotoView(ATPhotoViewActivity.this);
                view.setDrawingCacheEnabled(true);
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(ATPhotoViewActivity.this).load(imgList.get(position)).into(view);
                container.addView(view);
//                view.setOnClickListener(v -> {
//                    dialog = new Dialog(ATPhotoViewActivity.this, R.style.ActionSheetDialogStyle);
//                    @SuppressLint("InflateParams")
//                    View inflate = LayoutInflater.from(ATPhotoViewActivity.this).inflate(R.layout.preservationphoto, null);
//                    dialog.setContentView(inflate);
//                    Window dialogWindow = dialog.getWindow();
//                    dialogWindow.setGravity(Gravity.BOTTOM);
//                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//                    lp.y = 24;
//                    dialogWindow.setAttributes(lp);
//                    dialog.show();
//                    inflate.findViewById(R.id.perservati).setOnClickListener(v1 -> {
//                        view.setDrawingCacheEnabled(true);
//                        final Bitmap imageBitmap = view.getDrawingCache();
//                        if (imageBitmap != null) {
//                            ATLJSavePicture.saveImageToGallery(ATPhotoViewActivity.this, imageBitmap);
//                            dialog.dismiss();
//                        }
//                    });
//                    inflate.findViewById(R.id.cancel).setOnClickListener(v1 -> dialog.dismiss());
//                });
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                titlebar.setTitle(position + 1 + "/" + imgList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewpager.setCurrentItem(i);
    }
}

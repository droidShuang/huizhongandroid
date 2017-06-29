package com.junova.huizhong.adapter;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.junova.huizhong.R;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.common.SDCardImageLoader;
import com.junova.huizhong.viewCache.CheckGridViewCache;
import com.junova.huizhong.widget.AppAlertDialog;
import com.junova.huizhong.widget.PicGridView;
import com.orhanobut.logger.Logger;

/**
 * 添加论坛帖子图片适配器
 *
 * @author Administrator
 */
public class AddYichangGridAdapter extends BaseAdapter {

    private PicGridView gridView;
    private List<String> imageList;
    private Activity activity;
    private ImageView productLogoImageView;
    private ImageView cancelButton;
    private SDCardImageLoader loader;
    private int pos; // 可以点击添加的位置
    private boolean isother;//用来判断是其他异常检测 还是一般检测

    public AddYichangGridAdapter(Activity activity, List<String> imageList,
                                 PicGridView gridView) {
        this.gridView = gridView;
        this.imageList = imageList;
        this.isother = false;
        // imageList.add("");

        this.activity = activity;
        loader = new SDCardImageLoader(480, 800);
    }

    public AddYichangGridAdapter(Activity activity, List<String> imageList,
                                 PicGridView gridView, boolean isother) {
        this.gridView = gridView;
        this.imageList = imageList;
        // imageList.add("");
        this.activity = activity;
        this.isother = isother;
        loader = new SDCardImageLoader(480, 800);
    }

    @Override
    public int getCount() {
        if(imageList.size()>=1){
            return 1;
        }
        return imageList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        CheckGridViewCache viewCache;
        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.common_grid_item, null);
            viewCache = new CheckGridViewCache(rowView);
            rowView.setTag(viewCache);
        } else {
            viewCache = (CheckGridViewCache) rowView.getTag();
        }
        productLogoImageView = viewCache.getImageView();
        if(isother) {
            productLogoImageView.setClickable(true);
            productLogoImageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.OnClickItem(position);
                    }
                }
            });
        }

        cancelButton = viewCache.getCancelBtn();
        if (imageList.size() == position) {
            cancelButton.setVisibility(View.GONE);
        } else {
            cancelButton.setVisibility(View.VISIBLE);
        }
        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO delete
                AppAlertDialog dialog = new AppAlertDialog(activity, false,
                        "删除", "确定删除该图片？", "确定", "取消", R.style.alert_dialog,
                        new AppAlertDialog.AppAlertDialogListener() {

                            @Override
                            public void onClick(View view) {
                                // TODO Auto-generated method stub
                                if (view.getId() == R.id.positiveButton) {
                                    //删除本地图片
                                    File file = new File(imageList.get(position));
                                    file.delete();
                                    imageList.remove(position);
                                    notifyDataSetChanged();
                                }
                            }
                        }, false);
                dialog.show();

            }
        });

        if (position == imageList.size()) {

         //   if (position == 0) {
                productLogoImageView.setImageResource(R.drawable.hz_img_xq_btn_add);
                if (isother) {
                    productLogoImageView.setClickable(true);

                }
                pos = position;
     //       }
//            else {
//                productLogoImageView.setVisibility(View.GONE);
//                productLogoImageView.setClickable(false);
//            }


        } else {

            final String logoUrl = imageList.get(position);
            Logger.d(logoUrl);
            productLogoImageView.setTag(logoUrl);
            Bitmap map = loader.loadDrawable(4, logoUrl,
                    new SDCardImageLoader.ImageCallback() {

                        @Override
                        public void imageLoaded(Bitmap imageDrawable) {
                            // TODO Auto-generated method stub
                            ImageView imageView = (ImageView) gridView
                                    .findViewWithTag(logoUrl);
                            Bitmap map = FunctionUtils.centerSquareScaleBitmap(
                                    imageDrawable, 100);
                            imageView.setImageBitmap(map);
                        }
                    });
            if (map != null) {
                Bitmap maps = FunctionUtils.centerSquareScaleBitmap(map, 100);
                productLogoImageView.setImageBitmap(maps);


            }
        }
        return rowView;
    }

    public int getPos() {
        return pos;
    }

    //监听器

    private ClickItemListener listener;

    public void setItemListener(ClickItemListener listener) {
        this.listener = listener;
    }

    public interface ClickItemListener {
        void OnClickItem(int positon);
    }

}

package com.junova.huizhong.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.junova.huizhong.R;
import com.junova.huizhong.activity.ActivieDetailActivity;
import com.junova.huizhong.common.FunctionUtils;
import com.junova.huizhong.common.SDCardImageLoader;
import com.junova.huizhong.widget.AppAlertDialog;
import com.junova.huizhong.widget.PicGridView;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.List;

/**
 * Created by rider on 2016/8/22 0022 10:03.
 * Description :
 */
public class ActiveImageAdapter extends BaseAdapter {
    private Activity context;
    private List<String> imagePaths;
    private PicGridView picGridView;
    private SDCardImageLoader loader;
    private OnClickListener onClickListener;
    private ActiveDetailAdapter activeDetailAdapter;
    private Boolean isComplted;
    int groupPosition;
    int childPosition;

    public ActiveImageAdapter(Activity context, List<String> imagePaths, PicGridView picGridView, ActiveDetailAdapter activeDetailAdapter, boolean isComplted, int groupPosition, int childPosition) {
        this.context = context;
        this.imagePaths = imagePaths;
        this.picGridView = picGridView;
        loader = new SDCardImageLoader(480, 800);
        this.activeDetailAdapter = activeDetailAdapter;
        this.isComplted = isComplted;
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
    }

    @Override
    public int getCount() {
        if (isComplted) {
            return imagePaths.size();
        }
        return imagePaths.size() + 1;
    }

    @Override
    public String getItem(int position) {
        return imagePaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageViewHolder imageViewHolder;

        convertView = View.inflate(context, R.layout.common_grid_item, null);
        imageViewHolder = new ImageViewHolder(convertView);

        imageViewHolder.onBind(position);
        return convertView;
    }

    private class ImageViewHolder {
        private ImageView addImage, deleteImage;
        private View view;

        public ImageViewHolder(View view) {
            addImage = (ImageView) view.findViewById(R.id.image);
            deleteImage = (ImageView) view.findViewById(R.id.cancel);
            this.view = view;
        }

        private void onBind(final int position) {
            if (position == imagePaths.size()) {
                addImage.setImageResource(R.drawable.hz_img_xq_btn_add);
                if (onClickListener != null) {
                    addImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickListener.onClick(position);
                        }
                    });
                }
                deleteImage.setClickable(false);
                deleteImage.setVisibility(View.INVISIBLE);
            } else {
                deleteImage.setClickable(true);
                addImage.setClickable(false);
                deleteImage.setVisibility(View.VISIBLE);
                deleteImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppAlertDialog dialog = new AppAlertDialog(context, false,
                                "删除", "确定删除该图片？", "确定", "取消", R.style.alert_dialog,
                                new AppAlertDialog.AppAlertDialogListener() {

                                    @Override
                                    public void onClick(View view) {
                                        // TODO Auto-generated method stub
                                        if (view.getId() == R.id.positiveButton) {
                                            //删除本地图片
                                            Logger.d("position" + position);
                                            File file = new File(imagePaths.get(position));
                                            file.delete();
                                            imagePaths.remove(position);
                                            if (activeDetailAdapter.getMap().get(groupPosition).containsKey(childPosition)) {
                                                activeDetailAdapter.getMap().get(groupPosition).remove(childPosition);
                                                if (activeDetailAdapter.getMap().get(groupPosition).size() == 0) {
                                                    activeDetailAdapter.getMap().remove(groupPosition);
                                                }
                                            }
                                            activeDetailAdapter.notifyDataSetChanged();
                                        }

                                    }
                                }, false);

                        dialog.show();
                    }
                });
                final String url = imagePaths.get(position);
                Logger.d("position      " + position + url);
                addImage.setTag(url);
                loader.loadDrawable(4, url, new SDCardImageLoader.ImageCallback() {
                    @Override
                    public void imageLoaded(Bitmap imageDrawable) {
                        ImageView imageView = (ImageView) view.findViewWithTag(url);
                        Bitmap bitmap = FunctionUtils.centerSquareScaleBitmap(imageDrawable, 100);
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        }
    }

    private void setOnClickListener(ActiveImageAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position);
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}

package com.junova.huizhong.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.junova.huizhong.AppConfig;
import com.junova.huizhong.R;
import com.junova.huizhong.model.ActiveContentParam;
import com.junova.huizhong.model.ActiveItemParam;
import com.junova.huizhong.widget.PicGridView;
import com.junova.huizhong.widget.TextPop;

import org.json.JSONObject;

public class ActiveDetailAdapter extends BaseExpandableListAdapter {
    private HashMap<Integer, HashMap<Integer, Integer>> map;
    List<ActiveItemParam> date;
    Activity context;
    private GridOnItemSelectedListener listener;
    private String activityId;


    public ActiveDetailAdapter(List<ActiveItemParam> date, Activity context, String activityId) {
        super();
        map = new HashMap<>();
        this.date = date;
        this.context = context;
        this.activityId = activityId;
    }

    public void updata(List<ActiveItemParam> date) {
        this.date = date;
        notifyDataSetChanged();
    }

    public HashMap<Integer, HashMap<Integer, Integer>> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer, HashMap<Integer, Integer>> map) {
        this.map = map;
    }

    public GridOnItemSelectedListener getListener() {
        return listener;
    }

    public void setListener(GridOnItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public Object getChild(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return date.get(arg0).getContents().get(arg1);
    }


    @Override
    public long getChildId(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return Integer.valueOf(String.valueOf(arg0) + String.valueOf(arg1));
    }


    @Override
    public View getChildView(final int arg0, final int arg1, boolean arg2, View arg3,
                             ViewGroup arg4) {
        final ActiveContentParam ap = date.get(arg0).getContents().get(arg1);
        if (AppConfig.prefs.getInt("station", 0) < 4) {// 领导
            arg3 = LayoutInflater.from(context).inflate(
                    R.layout.item_active_content_leader, null);
            TextView title = (TextView) arg3.findViewById(R.id.content_text);
            title.setText(ap.getTitle());
            TextView isFinished = (TextView) arg3
                    .findViewById(R.id.content_finished);
            String finish = context.getResources().getString(
                    ap.isCompleted() ? R.string.finish : R.string.un_finish);
            isFinished.setText(finish);

            isFinished.setTextColor(context.getResources().getColor(
                    ap.isCompleted() ? R.color.bottom_sel : R.color.red));
            return arg3;
        }
        String activityCache = AppConfig.prefs.getString(ap.getId(), "");
        if (!activityCache.equals("")) {
            String[] data = activityCache.split(";");
            ap.setDescribtion(data[0]);
            List<String> imageList = new ArrayList<>();

            for (String path :
                    data[1].split(",")) {
                imageList.add(path);
            }
            if (ap.getImagePaths().containsAll(imageList)) {
                ap.getImagePaths().removeAll(imageList);
            }
            ap.getImagePaths().addAll(imageList);
            //    ap.setImagePaths(imageList);
        }
        arg3 = LayoutInflater.from(context).inflate(
                R.layout.item_active_content, null);
        final LinearLayout detailLayout = (LinearLayout) arg3.findViewById(R.id.layout_detail);
        final String tag = arg3.toString();
        final EditText edtDescbile = (EditText) arg3.findViewById(R.id.edt_describle);
        edtDescbile.setText(ap.getDescribtion());
        edtDescbile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                date.get(arg0).getContents().get(arg1).setDescribtion(edtDescbile.getText().toString());


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ImageView ivChoseText = (ImageView) arg3.findViewById(R.id.bt_chose_text);
        TextView title = (TextView) arg3.findViewById(R.id.content_text);
        Button btSave = (Button) arg3.findViewById(R.id.bt_save);
        title.setText(ap.getTitle());

        final Button finish = (Button) arg3
                .findViewById(R.id.content_finish_btn);
        if (ap.isCompleted()) {
            if (TextUtils.isEmpty(activityCache)) {
                detailLayout.setVisibility(View.VISIBLE);
                finish.setBackgroundResource(R.drawable.wancheng_1);
                finish.setText(context.getResources().getString(R.string.finish));
                finish.setTextColor(context.getResources().getColor(R.color.white));
                ap.setCompleted(true);
                AppConfig.prefs.edit().putInt(tag.toString() + "click", 1).commit();
                detailLayout.setVisibility(View.GONE);
            }
        }
        finish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (ap.isCompleted()) {
                    Toast.makeText(context, "已完成，不可更改", Toast.LENGTH_SHORT).show();
                    return;
                }
                int n = AppConfig.prefs.getInt(tag.toString() + "click", 0) % 2;
                if (n == 0) {


                } else {

                    finish.setBackgroundResource(R.drawable.weiwancheng_1);
                    finish.setText(context.getResources().getString(
                            R.string.un_finish));
                    finish.setTextColor(context.getResources().getColor(
                            R.color.xuncha_name));
                    ap.setCompleted(false);
                    AppConfig.prefs.edit().putInt(tag.toString() + "click", 0)
                            .commit();

                }

            }
        });
        final PicGridView imageGrid = (PicGridView) arg3.findViewById(R.id.photo_grid);
        final ActiveImageAdapter activeImageAdapter = new ActiveImageAdapter(context, ap.getImagePaths(), imageGrid, ActiveDetailAdapter.this, ap.isCompleted(), arg0, arg1);
        imageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onSelectedListener(arg0, arg1, position);
            }
        });

        imageGrid.setAdapter(activeImageAdapter);
        btSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ap.getImagePaths().size() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("提示");
                    builder.setMessage("请先拍照！");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.create().show();
                    return;
                }
                finish.setBackgroundResource(R.drawable.wancheng_1);
                finish.setText(context.getResources().getString(
                        R.string.finish));
                finish.setTextColor(context.getResources().getColor(
                        R.color.white));
                ap.setCompleted(true);
                ap.setDescribtion(edtDescbile.getText().toString());
                if (map.containsKey(arg0)) {
                    if (map.get(arg0).containsKey(arg1)) {
                        map.get(arg0).remove(arg1);
                    }
                    if (map.get(arg0).size() == 0) {
                        map.remove(arg0);
                    }
                }
                AppConfig.prefs.edit().putInt(tag.toString() + "click", 1)
                        .commit();
                String imagePaths = "";
                for (String imagePath : activeImageAdapter.getImagePaths()
                        ) {
                    imagePaths = imagePaths + imagePath + ",";
                }
                AppConfig.prefs.edit().putString(ap.getId(), ap.getDescribtion() + ";" + imagePaths).commit();
                Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });
        return arg3;
    }


    @Override
    public int getChildrenCount(int arg0) {
        // TODO Auto-generated method stub
        return date.get(arg0).getContents().size();
    }


    @Override
    public Object getGroup(int arg0) {
        // TODO Auto-generated method stub
        return date.get(arg0);
    }


    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return date.size();
    }


    @Override
    public long getGroupId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }


    @Override
    public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {

        arg2 = LayoutInflater.from(context).inflate(
                R.layout.item_active_detail, null);
        ActiveItemParam ap = date.get(arg0);
        TextView name = (TextView) arg2.findViewById(R.id.active_item_name);
        name.setText(ap.getTitle());
        ImageView expand = (ImageView) arg2.findViewById(R.id.img_expand);
        if (arg1) {
            expand.setBackgroundResource(R.drawable.laxia_1);
        } else {
            expand.setBackgroundResource(R.drawable.shouqi_1);
        }

        return arg2;
    }


    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }

    public interface GridOnItemSelectedListener {
        void onSelectedListener(int groupPosition, int childPosition, int gridPosition);
    }

}

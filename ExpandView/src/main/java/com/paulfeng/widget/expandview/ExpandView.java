package com.paulfeng.widget.expandview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by paulfeng on 2017/7/31.
 */

public class ExpandView extends LinearLayout implements PopupWindow.OnDismissListener{
    private static final int defaultSubViewHeight = 250;
    private int dp1 = 0;

    private Context mContext;

    private List<ImageView> imgLists;
    private List<TextView> textLists;
    private int index;
    private OnChooseListener mOnChooseListener;
    private List<View> popContentViewList = new ArrayList<>();


    private PopupWindow pop;

    private OnFoldListener onFoldListener;
    private int subvieHeight = 0;
    private int titleTextViewLayout = R.layout.tab_title_layout;
    private LayoutInflater mInflate;
    private int expand_drop_list_item = R.layout.expand_drop_list_item;


    public void setOnFoldListener(OnFoldListener onFoldListener) {
        this.onFoldListener = onFoldListener;
    }

    public void setOnChooseListener(OnChooseListener mOnChooseListener) {
        this.mOnChooseListener = mOnChooseListener;
    }

    public ExpandView(Context context) {
        this(context, null);
    }

    public ExpandView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        setOrientation(LinearLayout.HORIZONTAL);
        mInflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dp1 = (int) (0.5f + getResources().getDisplayMetrics().density);
        imgLists = new ArrayList<>();
        textLists = new ArrayList<>();
        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.ExpandView);
            subvieHeight = (int)a.getDimension(R.styleable.ExpandView_childViewHeight, defaultSubViewHeight * dp1);
            titleTextViewLayout = a.getResourceId(R.styleable.ExpandView_titleTextViewLayout, R.layout.tab_title_layout);
            expand_drop_list_item = a.getResourceId(R.styleable.ExpandView_subListviewItemLayout, R.layout.expand_drop_list_item);
            checkTitleValid(titleTextViewLayout);
            checkListItemLayoutValid(expand_drop_list_item);
        } finally {
            if(a != null)
            a.recycle();
        }
    }

    private void checkListItemLayoutValid(int expand_drop_list_item) {
        View  listitem =   mInflate.inflate(expand_drop_list_item, this, false);
        View tv = listitem.findViewById(R.id.tv);

        View iv = listitem.findViewById(R.id.iv);
        if(tv == null ||!(tv instanceof TextView) || iv == null || !(iv instanceof ImageView)){
            throw new  RuntimeException("must has a textview which id is title and a imgeview which id is title_pic");
        }
    }

    private void checkTitleValid(int titleTextViewLayout) {
        View  tabView =   mInflate.inflate(titleTextViewLayout, this, false);
        View titleView = tabView.findViewById(R.id.title);

        View title_pic = tabView.findViewById(R.id.title_pic);
        if(tabView == null ||!(titleView instanceof TextView) || title_pic == null || !(title_pic instanceof ImageView)){
            throw new  RuntimeException("must has a textview which id is title and a imgeview which id is title_pic");
        }
    }


    //the First Init Method
    public void setTitleAndContent(Map<String, List<String>> map) {
        int i = 0;

        Iterator<Map.Entry<String, List<String>>> it = map.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            String title = entry.getKey();
            List<String> itemList = entry.getValue();
            initDropMenuList(mInflate, itemList, i);
            RelativeLayout tabView = (RelativeLayout) mInflate.inflate(titleTextViewLayout, this, false);
            TextView titleView = (TextView) tabView.findViewById(R.id.title);
            ImageView title_pic = (ImageView) tabView.findViewById(R.id.title_pic);
            //set Tag for imageView animi control
            title_pic.setTag(false);
            titleView.setText(title);
            imgLists.add(title_pic);
            textLists.add(titleView);
            addView(tabView);
            tabView.setTag(i);
            tabView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    index = (int) view.getTag();
                    showDropMenuList(index);
                    imgAnmi(index);
                }
            });
            i++;
        }

    }

    private void initDropMenuList(LayoutInflater mInflate, final List<String> itemList, final int titleIndex) {
        LinearLayout view = (LinearLayout) mInflate.inflate(R.layout.expand_drop_list_layout, this, false);
        ListView lv = (ListView) view.findViewById(R.id.lv);
        ExpandListAdapter listAdapter = new ExpandListAdapter(mContext, expand_drop_list_item, itemList);
        lv.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(new ExpandListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                String selected = itemList.get(i);
                textLists.get(titleIndex).setText(selected);
                showDropMenuList(titleIndex);
                restoreImg(index);
                if(mOnChooseListener != null){
                    mOnChooseListener.onChoose(textLists , index);
                }
            }
        });


        int listviewHeight = getListViewHeightBasedOnChildren(lv);
        int xmlDefHeight = subvieHeight != 0 ? subvieHeight : dp1 * defaultSubViewHeight;

        if(listviewHeight > xmlDefHeight) {
            LayoutParams layoutParams = (LayoutParams) lv.getLayoutParams();
            layoutParams.height = xmlDefHeight;
            lv.setLayoutParams(layoutParams);
        }

        popContentViewList.add(view);

    }



    private void imgAnmi(int pos) {
        ImageView imageView = imgLists.get(pos);
        boolean isAnmied = (boolean) imageView.getTag();
        if(!isAnmied) {
            exeImgAnmi(imageView, true);
        }
        imageView.setTag(true);
        reInitOtherImg(pos);

    }

    private void reInitOtherImg(int pos) {
        for(int i = 0, n = imgLists.size(); i < n; i++) {
            ImageView img = imgLists.get(i);

            if(i != pos) {
                boolean flag = (boolean) img.getTag();
                if(flag) {
                    exeImgAnmi(img, false);
                    img.setTag(false);
                }
            }
        }
    }

    private void restoreImg(int pos) {
        ImageView imageView = imgLists.get(pos);
        exeImgAnmi(imageView, false);
        imageView.setTag(false);
    }


    private void exeImgAnmi(ImageView imageView, boolean openAnmi) {
        Animation anmi = openAnmi ? AnimationUtils.loadAnimation(mContext, R.anim.img_choose) : AnimationUtils.loadAnimation(mContext, R.anim.img_nochoose);
        anmi.setFillAfter(true);
        imageView.startAnimation(anmi);
        anmi.startNow();
    }


    @Override
    public void onDismiss() {
        if(onFoldListener != null) {
            onFoldListener.onFold();
        }
    }

    public interface OnChooseListener {
        void onChoose(List<TextView> textLists, int pos);
    }



    private void showDropMenuList(int index) {
        if (pop == null) {
            TextView textView = new TextView(mContext);
            textView.setText("sdfksd;f");
            textView.setTextColor(Color.BLACK);
            pop = new PopupWindow(popContentViewList.get(index), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            pop.setAnimationStyle(R.style.popAnim);
            pop.setFocusable(false);
            pop.setOutsideTouchable(true);
            pop.setOnDismissListener(this);
        }
        if (pop.isShowing()) {
            pop.dismiss();
            return;
        }

        if (pop.getContentView() != popContentViewList.get(index)) {
            pop.setContentView( popContentViewList.get(index));
        }
        pop.showAsDropDown(this, 0, 0);

    }

    public interface OnFoldListener {
        void onFold();
    }




//    private void dimBackground(Context mContext, boolean isDim) {
//        if(mContext instanceof Activity) {
//            Activity activity = (Activity) mContext;
//            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
//            lp.alpha = isDim ? 0.7f : 1f;
//            activity.getWindow().setAttributes(lp);
//        }
//    }

    public  int getListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        totalHeight = (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        return totalHeight;
    }

}

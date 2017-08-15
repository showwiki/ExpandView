package com.paulfeng.widget.expandview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


class ExpandListAdapter extends ArrayAdapter<String> {
    private OnClickListener onClickListener;
    private OnItemClickListener mOnItemClickListener;


    private Context mContext;

    private String seletedItem;

    List<String> itemList;
    int expand_drop_list_item = R.layout.expand_drop_list_item;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public ExpandListAdapter(Context mContext, int expand_drop_list_item, List<String> itemList) {
        super(mContext, expand_drop_list_item, itemList);
        this.expand_drop_list_item = expand_drop_list_item;
        this.mContext = mContext;
        seletedItem = itemList.get(0);
        this.itemList = itemList;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHoder viewHoder;
        if(convertView == null) {
            viewHoder = new ViewHoder();
            convertView = LayoutInflater.from(mContext).inflate(expand_drop_list_item, parent, false);
            viewHoder.img = (ImageView) convertView.findViewById(R.id.iv);
            viewHoder.text = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(viewHoder);
        } else {
            viewHoder = (ViewHoder)convertView.getTag();
        }
        String itemText = itemList.get(position);
        viewHoder.text.setText(itemText);
        viewHoder.img.setVisibility(seletedItem.equals(itemText) ? View.VISIBLE : View.GONE);

        viewHoder.text.setTag(position);

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = (int) view.findViewById(R.id.tv).getTag();
                setSelected(index);
                if(mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, index);
                }
            }
        });

        return convertView;
    }

    private void setSelected(int index) {
        if(itemList != null && itemList.size() > index) {
            seletedItem = itemList.get(index);
            notifyDataSetChanged();
        }
    }


    class ViewHoder {
        private ImageView img;
        private TextView text;
    }
}
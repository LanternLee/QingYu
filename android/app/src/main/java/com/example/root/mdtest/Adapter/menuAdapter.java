package com.example.root.mdtest.Adapter;

import com.example.root.mdtest.Model.MenuItem;
import com.example.root.mdtest.Model.User;
import com.example.root.mdtest.R;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2017/7/11.
 */

public class menuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MenuItem> menuItem;
    private Context context;
    private OnItemClickListener clickListener;

    public void setClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public menuAdapter(Context context, ArrayList<MenuItem> menu) {
        this.context = context;
        menuItem = menu;
    }

    @Override
    public int getItemCount() {
        return menuItem.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHolder viewHolder = new ItemHolder(LayoutInflater.from(context).inflate(R.layout.menu_list_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemHolder mHolder = (ItemHolder) holder;
        if (clickListener != null) {
            mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, position);
                }
            });
        }

        mHolder.icon.setImageDrawable(context.getDrawable(menuItem.get(position).menuIcon));
        mHolder.text.setText(menuItem.get(position).menuText);
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView text;
        CardView cardView;

        public ItemHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.list_item_icon);
            text = (TextView) view.findViewById(R.id.list_item_text);
            cardView = (CardView) view.findViewById(R.id.menu_item_card);

            cardView.setRadius(8);
            cardView.setCardElevation(8);
            cardView.setContentPadding(5, 5, 5, 5);
        }
    }
}

package com.example.root.mdtest.Adapter;

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

public class menuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<String> menuItem;
    private HeadHolder header;
    private Context context;
    private OnItemClickListener clickListener;
    private User user;
    private static final int HEADER = 422;
    private static final int ITEM = 502;

    public void setClickListener(OnItemClickListener listener){
        this.clickListener=listener;
    }

    public menuAdapter(Context context, ArrayList<String> menu,User user){
        this.context=context;
        menuItem=menu;
        this.user=user;
    }

    @Override
    public int getItemViewType(int position){
        if(position==0){
            return HEADER;
        }
        else return ITEM;
    }
    @Override
    public int getItemCount(){
        return menuItem.size()+1;
    }


    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Log.d("viewType",""+viewType);
        if(viewType==HEADER){
            if(header==null){
                header=new HeadHolder(LayoutInflater.from(context).inflate(R.layout.menu_list_header,parent,false));
            }
            return header;
        }
        else if(viewType==ITEM) {
            ItemHolder viewHolder = new ItemHolder(LayoutInflater.from(context).inflate(R.layout.menu_list_item, parent, false));
            return viewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position){
        if(position==0){
            if(user==null){
                return;
            }
            HeadHolder head=(HeadHolder)holder;
            head.name.setText(user.mail);
            head.credit.setText("信用分: "+user.score);
            head.has_borrow.setText("借伞: "+user.has_borrow);
            head.all_borrow.setText("总借伞: "+user.ncount);
            return;
        }

        ItemHolder mHolder=(ItemHolder) holder;
        if(clickListener!=null){
            mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view,position);
                }
            });
        }

        mHolder.icon.setImageDrawable(context.getDrawable(R.mipmap.ic_launcher));
        mHolder.text.setText(menuItem.get(position-1));
    }

    class HeadHolder extends RecyclerView.ViewHolder{
        ImageView portrait;
        TextView name;
        TextView credit;
        TextView has_borrow;
        TextView all_borrow;

        public HeadHolder(View view){
            super(view);
            portrait=(ImageView)view.findViewById(R.id.portrait);
            name=(TextView)view.findViewById(R.id.name);
            credit=(TextView)view.findViewById(R.id.credit);
            has_borrow=(TextView)view.findViewById(R.id.has_borrow);
            all_borrow=(TextView)view.findViewById(R.id.all_borrow);
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView text;
        CardView cardView;
        public ItemHolder(View view){
            super(view);
            icon=(ImageView)view.findViewById(R.id.list_item_icon);
            text=(TextView)view.findViewById(R.id.list_item_text);
            cardView=(CardView)view.findViewById(R.id.menu_item_card);

            cardView.setRadius(8);
            cardView.setCardElevation(8);
            cardView.setContentPadding(5,5,5,5);
        }
    }
}

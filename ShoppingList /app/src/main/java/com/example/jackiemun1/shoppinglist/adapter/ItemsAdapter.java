package com.example.jackiemun1.shoppinglist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jackiemun1.shoppinglist.MainActivity;
import com.example.jackiemun1.shoppinglist.R;
import com.example.jackiemun1.shoppinglist.data.AppDatabase;
import com.example.jackiemun1.shoppinglist.data.Item;

import java.util.Collections;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivIcon;
        public TextView tvName;
        public TextView tvPrice;
        public CheckBox cbStatusView;
        public TextView tvDescription;
        public Button btnEditItem;
        public Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            cbStatusView = itemView.findViewById(R.id.cbStatusView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnEditItem = itemView.findViewById(R.id.btnEditItem);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    private List<Item> itemsList;
    private Context context;
    private int lastPosition = -1;
    private double totalExpenseSum = 0;

    public ItemsAdapter(List<Item> itemsList, Context context) {
        this.itemsList = itemsList;
        this.context = context;
        totalExpenseSum = totalExpense(itemsList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.tvName.setText(getItem(position).getItemName());
        viewHolder.tvPrice.setText(context.getString(R.string.priceText)
                + Double.toString(getItem(position).getItemPrice()));
        viewHolder.ivIcon.setImageResource(getItem(position).getItemTypeAsEnum().getIconId());
        viewHolder.tvDescription.setText(context.getString(R.string.descriptText)
                + getItem(position).getDescription());
        viewHolder.cbStatusView.setChecked(itemsList.get(position).getItemStatus());
        viewHolder.cbStatusView.setEnabled(false);

        viewHolder.btnEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.cbStatusView.setChecked(itemsList.get(position).getItemStatus());
                ((MainActivity)context).showEditItemDialog(getItem(viewHolder.getAdapterPosition()));
                editTotalExpense(getItem(position));
            }
        });
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(viewHolder.getAdapterPosition());
            }
        });

        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void addItem(Item item) {
        itemsList.add(item);
        notifyDataSetChanged();
        addToTotalExpense(item);
    }

    public void updateItem(Item item) {
        int editPos = findItemIndexByItemId(item.getItemID());
        itemsList.set(editPos, item);
        notifyItemChanged(editPos);
    }

    private int findItemIndexByItemId(long itemId) {
        for(int i=0; i < itemsList.size(); i++) {
            if (itemsList.get(i).getItemID() == itemId) {
                return i;
            }
        }
        return -1;
    }

    public void removeItem(int position) {
        final Item itemToDelete = getItem(position);
        itemsList.remove(itemToDelete);
        notifyItemRemoved(position);
        subtractFromTotalExpense(itemToDelete);
        new Thread() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(context).itemDao().delete(itemToDelete);
            }
        }.start();
        ((MainActivity)context).setUpItemCount();
    }

    public void deleteAll() {

        int size = itemsList.size();
        for (int i = 0; i < size; i++) {
            final Item itemToDelete = getItem(i);
            notifyItemRemoved(i);
            totalExpenseSum = 0;
            new Thread() {
                @Override
                public void run() {
                    AppDatabase.getAppDatabase(context).itemDao().delete(itemToDelete);
                }
            }.start();
        }
        itemsList.clear();
    }

    public double totalExpense (List<Item> list) {
        double sum = 0;
        for (int i=0; i < list.size(); i++) {
            if(list.get(i).getItemStatus()) {
                sum += list.get(i).getItemPrice();
            }
        }
        return sum;
    }

    public void editTotalExpense(Item item) {
        if (item.getItemStatus()) {
            totalExpenseSum -= item.getItemPrice();
        } else {
            totalExpenseSum += item.getItemPrice();
        }
    }

    public void addToTotalExpense(Item item) {
        if (item.getItemStatus()) {
            totalExpenseSum += item.getItemPrice();
        }
    }

    public void subtractFromTotalExpense(Item item) {
        if (item.getItemStatus()) {
            totalExpenseSum -= item.getItemPrice();
        }
    }

    public double getTotalExpense() {
       return totalExpenseSum;
    }

    public void swapPlaces(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(itemsList, i, i + 1);
            }
        } else {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(itemsList, i, i - 1);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
    }

    public Item getItem(int i) {return itemsList.get(i);}

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context,
                    android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}

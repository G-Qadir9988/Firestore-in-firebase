
package com.example.lab11;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private final List<DashboardItem> itemList;
    private final Context context;

    public DashboardAdapter(Context context, List<DashboardItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single card item
        View view = LayoutInflater.from(context).inflate(R.layout.item_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DashboardItem item = itemList.get(position);
        holder.title.setText(item.title);
        holder.icon.setImageResource(item.iconResId);

        // Set the click listener to open the target activity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, item.targetActivity);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iv_option_icon);
            title = itemView.findViewById(R.id.tv_option_title);
        }
    }
}
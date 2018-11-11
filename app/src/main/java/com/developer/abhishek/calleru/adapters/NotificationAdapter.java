package com.developer.abhishek.calleru.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.abhishek.calleru.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.CustomNotificationAdapter>{

    private List<String> notificationList;

    public NotificationAdapter(List<String> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public CustomNotificationAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_list_item,viewGroup,false);
        return new CustomNotificationAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomNotificationAdapter customNotificationAdapter, int position) {
        if(notificationList.size()>position){
            customNotificationAdapter.notificationTv.setText(notificationList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class CustomNotificationAdapter extends RecyclerView.ViewHolder{

        @BindView(R.id.notificationTv)
        TextView notificationTv;

        public CustomNotificationAdapter(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

package com.developer.abhishek.calleru.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.abhishek.calleru.R;
import com.developer.abhishek.calleru.models.Contacts;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.CustomContactAdapter>{

    private final List<Contacts> contactsList;

    public ContactAdapter(List<Contacts> contactsList) {
        this.contactsList = contactsList;
    }

    @NonNull
    @Override
    public CustomContactAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_list_item,viewGroup,false);
        return new CustomContactAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomContactAdapter customContactAdapter, int position) {
        if(contactsList != null && contactsList.get(position) != null){
            customContactAdapter.contactName.setText(contactsList.get(position).getName());
            customContactAdapter.firstLetter.setText(contactsList.get(position).getName().substring(0,1));
        }
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public class CustomContactAdapter extends RecyclerView.ViewHolder{

        @BindView(R.id.contactName)
        TextView contactName;
        @BindView(R.id.firstLetterTv)
        TextView firstLetter;

        public CustomContactAdapter(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

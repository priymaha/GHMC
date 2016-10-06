package com.example.priyanka.ghmc.adapter;

/**
 * Created by Priyanka on 03/10/16.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.example.priyanka.ghmc.model.DataModel;
import com.example.priyanka.ghmc.model.GrievanceStatusModel;
import com.example.priyanka.ghmc.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RecyclerViewSectionAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {


    private List<DataModel> allData = Collections.emptyList();


    public RecyclerViewSectionAdapter(List<DataModel> data) {

        this.allData = data;
    }


    @Override
    public int getSectionCount() {
        return allData.size();
    }

    @Override
    public int getItemCount(int section) {

        return allData.get(section).getAllItemsInSection().size();

    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {

        String sectionName = allData.get(section).getHeaderTitle();
        SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
        sectionViewHolder.sectionTitle.setText(sectionName);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, int relativePosition, int absolutePosition) {

        ArrayList<GrievanceStatusModel> itemsInSection = allData.get(section).getAllItemsInSection();

        String itemTitle = itemsInSection.get(relativePosition).title;
        String itemType = itemsInSection.get(relativePosition).type;

        String itemTime= itemsInSection.get(relativePosition).time;
       String membersJoined = itemsInSection.get(relativePosition).members;

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        itemViewHolder.itemTitle.setText(itemTitle);
        itemViewHolder.itemType.setText(itemType);
        itemViewHolder.itemTime.setText(itemTime);
        itemViewHolder.membersJoined.setText(membersJoined);

        // Try to put a image . for sample i set background color in xml layout file
        // itemViewHolder.itemImage.setBackgroundColor(Color.parseColor("#01579b"));
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Change inflated layout based on 'header'.
        if (viewType == VIEW_TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grievance_status_header, parent, false);
            return new SectionViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.grievance_status_item, parent, false);
            return new ItemViewHolder(v);
        }

    }


    // SectionViewHolder Class for Sections
    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        final TextView sectionTitle;
        public SectionViewHolder(View itemView) {
            super(itemView);
            sectionTitle = (TextView) itemView.findViewById(R.id.grievance_status_date);
        }
    }

    // ItemViewHolder Class for Items in each Section
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        final TextView itemTitle;
        final ImageView itemImage;
        final TextView itemTime;
        final TextView itemType;
        final TextView membersJoined;


        public ItemViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.status_title);
            itemTime = (TextView) itemView.findViewById(R.id.status_time);
            itemType = (TextView) itemView.findViewById(R.id.status_type);
            itemImage = (ImageView) itemView.findViewById(R.id.status_iv);
            membersJoined = (TextView) itemView.findViewById(R.id.members_joined);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(v.getContext(), itemTitle.getText(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
package com.ecbclass.database;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.quickstart.database.R;

import java.util.ArrayList;


public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {

    private ArrayList<String> subjectList;
    private String selectedStream;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView subjectName;
        public Button button;

        public MyViewHolder(final View view) {
            super(view);
            subjectName = (TextView) view.findViewById(R.id.subject_list_butns);
            button = (Button) view.findViewById(R.id.subject_list_butns);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String subjectName = subjectList.get(getAdapterPosition());
            Intent intent = new Intent(v.getContext(), PostBySubject.class);
            intent.putExtra("selectedStream", selectedStream);
            intent.putExtra("selectedSubject", subjectName);
            v.getContext().startActivity(intent);
        }
    }


    public SubjectAdapter(ArrayList<String> subjectList, String selectedStream) {
        this.subjectList = subjectList;
        this.selectedStream = selectedStream;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_subject_recycle_view_holder, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String subject = subjectList.get(position);
        holder.subjectName.setText(subject);
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }
}
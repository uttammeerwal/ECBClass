package com.ecbclass.database;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.ecbclass.user_activity.Home;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.quickstart.database.R;

import java.util.ArrayList;
import java.util.List;


public class Branch extends Home implements View.OnClickListener {
    private static String selectedStream = "Select Stream";
    private ProgressDialog progress;
    private Spinner streamSpinner;
    private static ArrayList subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_branch);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameHome);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View inflate = layoutInflater.inflate(R.layout.activity_branch, null, true);
        frameLayout.addView(inflate);

        progress = new ProgressDialog(this);
        subjects = new ArrayList<String>();
        List<String> streamList = new ArrayList();
        streamList.add("Select Stream");
        streamList.add("Electronics And Communication");
        streamList.add("Electrical");
        streamList.add("Electronics Instrumentation & Control");
        streamList.add("Computer Science");
        streamList.add("Information Technology");
        streamList.add("Mechanical");
        streamList.add("Civil");
        streamSpinner = (Spinner) findViewById(R.id.stream_branch_spinner);

        final ArrayAdapter<String> classArrayAdapter = new ArrayAdapter(getApplicationContext(),
                R.layout.spinner_dropdown_item, streamList);
        classArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        streamSpinner.setAdapter(classArrayAdapter);
        streamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (classArrayAdapter.getItem(position).equalsIgnoreCase("select Stream")) {
                    subjects.removeAll(subjects);
                    updateRecyclerView(subjects);
                } else {
                    subjects.removeAll(subjects);
                    updateRecyclerView(subjects);
                    Snackbar bar = Snackbar.make(findViewById(R.id.stream_branch_spinner), "Stream : "
                            + classArrayAdapter.getItem(position), Snackbar.LENGTH_LONG);
                    View sbView = bar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                            R.color.colorPrimaryDark));
                    bar.show();
                    selectedStream = classArrayAdapter.getItem(position);
                    showHideProgressDialog(true);
                    updateSubjects();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    void updateSubjects() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("posts-by-subjects").child(selectedStream).child("subjects");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        subjects.add(child.getValue().toString());
                    }

                    updateRecyclerView(subjects);
                    showHideProgressDialog(false);
                } else {
                    showHideProgressDialog(false);
                    Snackbar bar = Snackbar.make(findViewById(android.support.design.R.id.snackbar_text),
                            "No post for Subjects having this stream selection",
                            Snackbar.LENGTH_LONG);
                    View sbView = bar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                            R.color.pure_red));
                    bar.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateRecyclerView(ArrayList<String> subjects) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.subject_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new SubjectAdapter(subjects, selectedStream));
    }

    void showHideProgressDialog(boolean value) {

        if (value) {
            progress.setMessage("Loading... ");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        } else {
            progress.dismiss();
        }
    }
}

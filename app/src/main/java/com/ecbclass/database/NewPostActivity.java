package com.ecbclass.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.ecbclass.NetworkCheker;
import com.ecbclass.database.models.Post;
import com.ecbclass.database.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.quickstart.database.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class NewPostActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";
    public static String selectStream = "Select Stream";
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]
    private EditText mTitleField;
    private EditText mBodyField;
    private FloatingActionButton mSubmitButton;
    private Spinner subjectSpinner;
    private FirebaseAuth mAuth;
    private Spinner classSpinner;
    private List<String> subjectList = new ArrayList();
    private static ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        mAuth = FirebaseAuth.getInstance();
        setTitle("Create Post");

        progress = new ProgressDialog(this);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setMessage("Server not reachable.Check internet Connection");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]
        mTitleField = (EditText) findViewById(R.id.field_title);
        mBodyField = (EditText) findViewById(R.id.field_body);
        mSubmitButton = (FloatingActionButton) findViewById(R.id.fab_submit_post);
        classSpinner = (Spinner) findViewById(R.id.spinner);
        subjectSpinner = (Spinner) findViewById(R.id.spinner2);

        isOnlne();
        List<String> classList = new ArrayList();
        classList.add("Select Stream");
        classList.add("Electronics And Communication");
        classList.add("Electrical");
        classList.add("Electronics Instrumentation & Control");
        classList.add("Computer Science");
        classList.add("Information Technology");
        classList.add("Mechanical");
        classList.add("Civil");

        subjectList.add("Select Subject");

        final ArrayAdapter<String> classArrayAdapter = new ArrayAdapter(getApplicationContext(),
                R.layout.spinner_dropdown_item, classList);
        classArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(classArrayAdapter);

        final ArrayAdapter<String> subjectArrayAdapter = new ArrayAdapter(getApplicationContext(),
                R.layout.spinner_dropdown_item, subjectList);
        subjectArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectArrayAdapter);

        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (classArrayAdapter.getItem(position).equalsIgnoreCase("select Stream")) {
                    subjectSpinner.setAlpha(0.5f);
                    subjectSpinner.setEnabled(false);
                    mBodyField.setEnabled(false);
                    mTitleField.setEnabled(false);
                    mSubmitButton.setAlpha(0.5f);
                    mSubmitButton.setEnabled(false);
                } else {
                    Snackbar bar = Snackbar.make(view, "Stream: " + classArrayAdapter.getItem(position), Snackbar.LENGTH_SHORT);
                    View sbView = bar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                    bar.show();
//                    0xff00A7A5
                    selectStream = classArrayAdapter.getItem(position);
                    subjectSpinner.setAlpha(1.0f);
                    subjectSpinner.setEnabled(true);
                    showHideProgressDialog(true);
                    updateSubjectList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getSelectedItem().toString().equalsIgnoreCase("select subject")) {
                    mTitleField.setEnabled(false);
                    mBodyField.setEnabled(false);
                    mSubmitButton.setAlpha(0.5f);
                    mSubmitButton.setEnabled(false);
                } else {
//                    SpannableStringBuilder snackbarText = new SpannableStringBuilder();
//                    snackbarText.append("");
//                    int boldStart = snackbarText.length();
//                    snackbarText.append("Subject : ");
//                    snackbarText.setSpan(new ForegroundColorSpan(Color.parseColor("#0288D1")), boldStart, snackbarText.length(),
//                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    snackbarText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), boldStart,
//                            snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    snackbarText.append("" + parent.getSelectedItem().toString());
                    Snackbar bar = Snackbar.make(view, "Subject: " + parent.getSelectedItem().toString(), Snackbar.LENGTH_SHORT);
                    View sbView = bar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                    bar.show();
                    mSubmitButton.setAlpha(1.0f);
                    mSubmitButton.setEnabled(true);
                    mTitleField.setEnabled(true);
                    mBodyField.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }

    private void updateSubjectList() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("branch").child(selectStream).child("subjects");
        isOnlne();
        String firstSubject = subjectList.get(0);
        subjectList.removeAll(subjectList);
        subjectList.add(firstSubject);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Pattern PATTERN = Pattern.compile("@@", Pattern.LITERAL);

                try {
                    JSONArray jsonArray = new JSONArray(dataSnapshot.getValue().toString());
                    for (int i = 0; i <= jsonArray.length(); i++) {
                        if (!jsonArray.get(i).toString().equalsIgnoreCase("null")) {
                            String special = jsonArray.get(i).toString();
                            special = PATTERN.matcher(special).replaceAll(" ");
                            subjectList.add(special);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                showHideProgressDialog(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void submitPost() {
        final String title = mTitleField.getText().toString();
        final String body = mBodyField.getText().toString();
        final String subject = subjectSpinner.getSelectedItem().toString();
        final String stream = classSpinner.getSelectedItem().toString();
        final String userImage;
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String str = currentUser.getEmail();
        if (str != null && str != "") {
            userImage = currentUser.getPhotoUrl().toString();
        } else {
            userImage = "phone";
        }
        // Title is required
        if (TextUtils.isEmpty(title)) {
            mTitleField.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(body)) {
            mBodyField.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        // [START single_value_read]

        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = new User();
                        isOnlne();
                        // Get user value
                        try {
                            JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                            user.setEmail(jsonObject.getString("email"));
                            user.setUsername(jsonObject.getString("username"));

                        } catch (JSONException e) {
                            user = dataSnapshot.getValue(User.class);
                            e.printStackTrace();
                        }

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(NewPostActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            isOnlne();
                            if (!(classSpinner.getSelectedItem().toString().equalsIgnoreCase("select stream") &&
                                    subjectSpinner.getSelectedItem().toString().equalsIgnoreCase("select subject"))) {
                                // Write new post
                                Snackbar bar = Snackbar.make(findViewById(R.id.fab_submit_post),
                                        "Posting..........", Snackbar.LENGTH_LONG);
                                View sbView = bar.getView();
                                sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),
                                        R.color.colorAccent));
                                bar.show();
                                writeNewPost(userId, user.username, title, body, stream, subject, userImage);
                            } else {
                                Snackbar.make(findViewById(android.support.design.R.id.snackbar_text),
                                        "Stream name or subject name is not applicable", Snackbar.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        final Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setEditingEnabled(true);
                                finish();
                                // [END_EXCLUDE]
                            }
                        }, 1000);
                        // Finish this Activity, back to the stream

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void setEditingEnabled(boolean enabled) {
        mTitleField.setEnabled(enabled);
        mBodyField.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    // [START write_fan_out]
    private void writeNewPost(String userId, String username, String title, String body, String stream, String subject, String userImage) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body, stream, subject, userImage);
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);
        childUpdates.put("/stream/" + selectStream + "/subjects/" + subject + "/" + key, postValues);
        childUpdates.put("/posts-by-subjects/" + selectStream + "/subjects/" + subject, subject);
        isOnlne();
        mDatabase.updateChildren(childUpdates);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    // [END write_fan_out]

    void showHideProgressDialog(boolean value) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.subject_progressbar);
        if (value) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);

        }
    }

    public void isOnlne() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            changeStatus(true);
        } else {
            changeStatus(false);
        }
    }

    // Method to change the text status
    public void changeStatus(boolean isConnected) {
        // Change status according to boolean value
        if (isConnected) {
            progress.dismiss();
        } else {
            progress.show();
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        NetworkCheker.activityPaused();// On Pause notify the Application
    }

    @Override
    protected void onResume() {

        super.onResume();
        NetworkCheker.activityResumed();// On Resume notify the Application
    }


}

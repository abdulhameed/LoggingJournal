package com.example.android.loggingjournal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.android.loggingjournal.JournalDatabase.Journal;
import com.example.android.loggingjournal.JournalDatabase.JournalDatabase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class JournalListActivity extends AppCompatActivity implements JournalsAdapter.OnJournalItemClick {



    private TextView textViewMsg;
    private RecyclerView recyclerView;
    private JournalDatabase journalDatabase;
    private List<Journal> journals;
    private JournalsAdapter journalsAdapter;
    private int pos;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVies();
        displayList();
    }

    private void displayList(){
// initialize database instance
        journalDatabase = JournalDatabase.getInstance(JournalListActivity.this);
// fetch list of notes in background thread
        new RetrieveTask(this).execute();
    }

    @Override
    public void onJournalClick(final int pos) {

        new AlertDialog.Builder(JournalListActivity.this)
                .setTitle("Select Options")
                .setItems(new String[]{"Delete", "Update"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                journalDatabase.getJournalDAO().deleteJournals(journals.get(pos));
                                journals.remove(pos);
                                listVisibility();
                                break;
                            case 1:
                                JournalListActivity.this.pos = pos;
                                startActivityForResult(
                                        new Intent(JournalListActivity.this,
                                                AddJournalActivity.class).putExtra("journal", (Parcelable) journals.get(pos)),
                                        100);

                                break;
                        }
                    }
                }).show();

    }

    private static class RetrieveTask extends AsyncTask<Void,Void,List<Journal>> {

        private WeakReference<JournalListActivity> activityReference;

        // only retain a weak reference to the activity
        RetrieveTask(JournalListActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<Journal> doInBackground(Void... voids) {
            if (activityReference.get()!=null)
                return activityReference.get().journalDatabase.getJournalDAO().getJournals();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<Journal> journals) {
            if (journals!=null && journals.size()>0 ){
                activityReference.get().journals = journals;

                // hides empty text view
                activityReference.get().textViewMsg.setVisibility(View.GONE);

                // create and set the adapter on RecyclerView instance to display list
                activityReference.get().journalsAdapter = new JournalsAdapter(journals,activityReference.get());
                activityReference.get().recyclerView.setAdapter(activityReference.get().journalsAdapter);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initializeVies(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textViewMsg = (TextView) findViewById(R.id.tv__empty);

        // Action button to add note
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(listener);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(JournalListActivity.this));

        journals = new ArrayList<>();
        journalsAdapter = new JournalsAdapter(journals,JournalListActivity.this);
        recyclerView.setAdapter(journalsAdapter);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivityForResult(new Intent(JournalListActivity.this,AddJournalActivity.class),100);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode > 0 ){
            if( resultCode == 1){
                journals.add((Journal) data.getSerializableExtra("note"));
            }else if( resultCode == 2){
                journals.set(pos,(Journal) data.getSerializableExtra("note"));
            }
            listVisibility();
        }
    }


//    @Override
//    public void OnJournalItemClick(final int pos) {
//        new AlertDialog.Builder(JournalListActivity.this)
//                .setTitle("Select Options")
//                .setItems(new String[]{"Delete", "Update"}, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        switch (i){
//                            case 0:
//                                journalDatabase.getJournalDAO().deleteJournals(journals.get(pos));
//                                journals.remove(pos);
//                                listVisibility();
//                                break;
//                            case 1:
//                                JournalListActivity.this.pos = pos;
//                                startActivityForResult(
//                                        new Intent(JournalListActivity.this,
//                                                AddJournalActivity.class).putExtra("journal", (Parcelable) journals.get(pos)),
//                                        100);
//
//                                break;
//                        }
//                    }
//                }).show();
//
//    }


    private void listVisibility(){
        int emptyMsgVisibility = View.GONE;
        if (journals.size() == 0){ // no item to display
            if (textViewMsg.getVisibility() == View.GONE)
                emptyMsgVisibility = View.VISIBLE;
        }
        textViewMsg.setVisibility(emptyMsgVisibility);
        journalsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        journalDatabase.cleanUp();
        super.onDestroy();
    }



    private void setSupportActionBar(Toolbar toolbar) {
    }


}

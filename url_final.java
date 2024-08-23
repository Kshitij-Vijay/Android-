package com.Xitij.pricescrapper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Xitij.pricescrapper.Adapters.group_select_interfac;
import com.Xitij.pricescrapper.Adapters.group_selection_adapter;
import com.Xitij.pricescrapper.Models.group_selection;
import com.Xitij.pricescrapper.web.JsoupAsyncTask;
import com.Xitij.pricescrapper.Database.DatabaseHelper;
import com.Xitij.pricescrapper.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class url_final extends AppCompatActivity
        implements group_select_interfac, JsoupAsyncTask.JsoupAsyncTaskListener {

    DatabaseHelper mydb;
    private String url;
    private String name;
    private ArrayList<String> groups = new ArrayList<>();
    private String desc;
    group_selection_adapter ear;
    private String notifinc;
    private String notifdec;
    private String freq;
    ArrayList<String> selected_groups = new ArrayList<String>();

    // @Override
    // public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    // int id = item.getItemId();
    // if(id == R.id.textView)
    // return super.onOptionsItemSelected(item);
    // }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(url_final.this, browse_act.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.url_final_page);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String price = intent.getStringExtra("price");

        mydb = new DatabaseHelper(url_final.this);

        TextInputEditText desctxt = findViewById(R.id.url_final_desc_input);
        TextInputEditText nametxt = findViewById(R.id.url_final_name_input);
        TextInputEditText freqtxt = findViewById(R.id.url_final_refresh_custom_frequencyInput);
        Switch notifSw = findViewById(R.id.url_final_notifSwitch);
        TextInputEditText notifinctxt = findViewById(R.id.url_final_notif_inc);
        TextInputEditText notifdectxt = findViewById(R.id.url_final_notif_dec);
        // RadioGroup radioGroup = findViewById(R.id.url_final_discard_radioGroup);

        RecyclerView rv = findViewById(R.id.url_final_groups_recyclerlayout);
        rv.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<String> group_data = mydb.get_groups();
        ArrayList<group_selection> data = new ArrayList<>();
        for (String ele : group_data) {
            group_selection dg = new group_selection(false, ele);
            data.add(dg);
        }
        ear = new group_selection_adapter(url_final.this, data, this);
        rv.setAdapter(ear);

        // ListView ListViewData;
        // ArrayAdapter<String> adapter;
        // ArrayList<String> raw_gdata = mydb.get_groups();
        // String[] raw_group_names = new String[raw_gdata.size()];
        // for(int i=0;i<raw_gdata.size();i++){
        // raw_group_names[i] = raw_gdata.get(i);
        // }
        //// ListViewData = findViewById(R.id.ListviewData);
        // adapter = new ArrayAdapter<String>(this,
        // android.R.layout.simple_list_item_multiple_choice,raw_group_names);
        //// ListViewData.setAdapter(adapter);
        //
        //
        //
        // ListViewData.setOnItemClickListener(new AdapterView.OnItemClickListener(){
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view, int position, long
        // id) {
        // String selectedItem = (String) parent.getItemAtPosition(position);
        // groups.add(selectedItem);
        // }
        // });

        Button addgroup = findViewById(R.id.url_final_addGroupbtn);
        addgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(url_final.this, popup.class);
                startActivity(intent);
                ArrayList<String> group_data = mydb.get_groups();
                // for(int i=0;i<raw_gdata.size();i++){
                // raw_group_names[i] = group_data.get(i);
                // }
                // ListView lvd = findViewById(R.id.ListviewData);
                // ArrayAdapter<String> adapt = new ArrayAdapter<String>(url_final.this,
                // android.R.layout.simple_list_item_multiple_choice,raw_group_names);
                // lvd.setAdapter(adapt);

                ArrayList<group_selection> data = new ArrayList<>();
                for (String ele : group_data) {
                    group_selection dg = new group_selection(false, ele);
                    data.add(dg);
                }
                group_selection_adapter ear = new group_selection_adapter(url_final.this, data, url_final.this);
                rv.setAdapter(ear);
            }
        });

        freqtxt.setText(mydb.get_default_frequency());
        notifSw.setChecked(true);
        notifinctxt.setText(mydb.get_default_notifinc());
        notifdectxt.setText(mydb.get_default_notifdec());

        notifSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked() == true) {
                    notifdectxt.setEnabled(true);
                    notifinctxt.setEnabled(true);
                    notifinctxt.setText(mydb.get_default_notifinc());
                    notifdectxt.setText(mydb.get_default_notifdec());
                } else {
                    notifdectxt.setEnabled(false);
                    notifinctxt.setEnabled(false);
                    notifdectxt.setText("");
                    notifinctxt.setText("");
                }
            }
        });

        Button donebtn = findViewById(R.id.url_final_save_button);
        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nametxt.getText().toString().trim();
                desc = desctxt.getText().toString().trim();
                if (name == null) {
                    Toast.makeText(url_final.this, "name must not be null", Toast.LENGTH_SHORT).show();
                } else {
                    freq = freqtxt.getText().toString();
                    if (freq == null) {
                        freq = mydb.get_default_frequency();
                    } else {
                    }

                    notifinc = "-1";
                    notifdec = "-1";

                    if (notifSw.isChecked() == true) {
                        // enabled so do everything
                        notifinc = notifinctxt.getText().toString();
                        notifdec = notifdectxt.getText().toString();
                    }

                    if (name != null && url != null && freq != null && notifdec != null && notifinc != null) {
                        try {
                            ArrayList<String> nemo = mydb.get_all_names();
                            if (nemo.contains(name) == true) {
                                Toast.makeText(url_final.this, "Name has aldready been taken", Toast.LENGTH_SHORT)
                                        .show();
                                nametxt.requestFocus();
                            } else {
                                mydb.inserturl(name, desc, "Amazon", "Amazon", url, freq, notifinc, notifdec);
                                groups = ear.getSelectedItems();
                                if (groups.isEmpty() == false) {
                                    int l = groups.size();
                                    for (int i = 0; i < l; i++) {
                                        mydb.insert_into_pgroup(name, groups.get(i));
                                        Log.d("groups final url ", "url final group : " + groups.get(i));
                                    }
                                }
                                String key = mydb.Vrudhi(url);
                                String[] myArr = { url, key };
                                // Toast.makeText(url_final.this, "before", Toast.LENGTH_SHORT).show();
                                new JsoupAsyncTask(url_final.this).execute(myArr); // one price is inserted
                                // Toast.makeText(url_final.this, "AFTER", Toast.LENGTH_SHORT).show();
                                setContentView(R.layout.loading);
                            }

                        } catch (Exception e) {
                            if (e.toString().equals("unique entries only")) {
                                Toast.makeText(url_final.this, "Enter another name...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }
            }
        });

        Button url_final_cancel_button = findViewById(R.id.url_final_cancel_button);
        url_final_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(url_final.this, browse_act.class);
                startActivity(intent1);
            }
        });

    }

    @Override
    public void groupSelect(int pos) {
        ArrayList<String> gdata = mydb.get_groups();
        String s = gdata.get(pos);
        selected_groups.add(s);
    }

    @Override
    public void onTaskComplete(String title, String url) {
        if (title.equals("null")) {
            Toast.makeText(this, "Something went wrong ...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(url_final.this, browse_act.class);
            startActivity(intent);
        } else {
            Log.d("Insertion", "I've reloaded");
            int p = Integer.parseInt(title);
            mydb.singleCycle(p, url);
            Intent intent1 = new Intent(url_final.this, browse_act.class);
            startActivity(intent1);
            Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show();
        }
    }
}

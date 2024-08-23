package com.Xitij.pricescrapper;

import com.Xitij.pricescrapper.Adapters.browse_group_interface;
import com.Xitij.pricescrapper.Adapters.itemAdapter;
import com.Xitij.pricescrapper.Adapters.item_interface;
import com.Xitij.pricescrapper.Database.cyclesService;
import com.Xitij.pricescrapper.web.JsoupAsyncTask;
import com.Xitij.pricescrapper.Adapters.groupAdapter;
import com.Xitij.pricescrapper.Database.DatabaseHelper;
import com.Xitij.pricescrapper.databinding.BrowsePageBinding;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdSize;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class browse_act extends AppCompatActivity
        implements browse_group_interface, item_interface, JsoupAsyncTask.JsoupAsyncTaskListener {

    private BrowsePageBinding binding;
    DatabaseHelper mydb;
    LinearLayout adContainerView;
    String prev_intent = null;

    @Override
    public void onBackPressed() {
        // Toast.makeText(this, "back pressed " + prev_intent,
        // Toast.LENGTH_SHORT).show();
        if (prev_intent == null) {
            super.onBackPressed();
        } else if (prev_intent.equals("home")) {
            Intent it = new Intent(browse_act.this, home.class);
            it.putExtra("prev_intent", "report");
            startActivity(it);
        } else if (prev_intent.equals("search")) {
            Intent it = new Intent(browse_act.this, search.class);
            it.putExtra("prev_intent", "report");
            startActivity(it);
        } else if (prev_intent.equals("browse")) {
            // Toast.makeText(this, "inside " + prev_intent, Toast.LENGTH_SHORT).show();
            Intent it = new Intent(browse_act.this, browse_act.class);
            it.putExtra("prev_intent", "report");
            startActivity(it);
        } else if (prev_intent.equals("groups")) {
            // Toast.makeText(this, "inside " + prev_intent, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BrowsePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent2 = getIntent();
        prev_intent = intent2.getStringExtra("prev_intent");

        // service
        Intent serviceIntent = new Intent(browse_act.this, cyclesService.class);
        startService(serviceIntent);

        adContainerView = findViewById(R.id.ads_container);

        // binding
        binding.browsePageNavBar.setOnItemSelectedListener(item -> {
            final int v = item.getItemId();
            if (v == R.id.searchMenu) {
                // Toast.makeText(this, "Search page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(browse_act.this, search.class);
                intent.putExtra("prev_intent", "browse");
                startActivity(intent);
            } else if (v == R.id.browseMenu) {
                // Toast.makeText(this, "Browse page", Toast.LENGTH_SHORT).show();
                recreate();
            } else if (v == R.id.setingsMenu) {
                Intent intent = new Intent(browse_act.this, settings.class);
                intent.putExtra("prev_intent", "browse");
                startActivity(intent);
            }
            return true;
        });

        // all other stuff

        RecyclerView rv = findViewById(R.id.all_list_container);
        rv.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView rvg = findViewById(R.id.Groups_recycler);
        rvg.setLayoutManager(new LinearLayoutManager(this));

        mydb = new DatabaseHelper(this);

        ArrayList<String> names = mydb.get_all_names();
        itemAdapter ear = new itemAdapter(browse_act.this, names, this);
        rv.setAdapter(ear);

        ArrayList<String> groups = mydb.get_groups();
        groupAdapter etr = new groupAdapter(browse_act.this, groups, this);
        rvg.setAdapter(etr);

        // Button load_ad = findViewById(R.id.load_ad);
        // load_ad.setOnClickListener(new View.OnClickListener() {
        // @Override
        // public void onClick(View v) {
        // loadBanner();
        // DatabaseHelper mydb = null;
        // try {
        // mydb = new DatabaseHelper(getApplicationContext());
        // ArrayList<String> names = mydb.get_all_names();
        //// mydb.cat(names);
        // List<String[]> data1 = new ArrayList<>();
        // for (String name : names) {
        // data1.add(new String[]{String.valueOf(mydb.get_index(name)),"",
        // mydb.get_link_from_name(name)});
        //// Log.d("something", Arrays.toString(new
        // String[]{String.valueOf(mydb.get_index(name)),"",
        // mydb.get_link_from_name(name)}));
        // }
        // Storage.saveToFile(getApplicationContext(), data1);
        // initiate();
        // }catch (Exception e){
        // e.printStackTrace();
        // }
        // }
        // Intent serviceIntent = new Intent(browse_act.this, selfService.class);
        // serviceIntent.putExtra("key", "a"); // Put your data here
        // Log.d("SSSS", "Doing service to self");
        // startService(serviceIntent);
        // }
        // });

        FloatingActionButton addnewlink_button = findViewById(R.id.add_newLink);
        addnewlink_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url_switch(v);
            }
        });
    }

    public void url_switch(View view) {
        setContentView(R.layout.url_input_page);
        prev_intent = "browse";

        TextInputEditText url_input = findViewById(R.id.url_input);

        Button done_btn = findViewById(R.id.url_input_done_button);
        Button url_input_cancel_button = findViewById(R.id.url_input_cancel_button);

        final String[] url = new String[1];

        final TextView[] text = { findViewById(R.id.desc_url_text) };

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url[0] = url_input.getText().toString();
                String key = mydb.Vrudhi(url[0]);
                String[] myArr = { url[0], key };
                new JsoupAsyncTask(browse_act.this).execute(myArr);
                setContentView(R.layout.loading);
            }
        });

        url_input_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
    }

    public void url_save_page(int n, String s) {
        setContentView(R.layout.url_save_page);// changing act
        prev_intent = "browse";

        TextView priceTxt = findViewById(R.id.url_save_pricetext);

        Button url_save_cancel_button = findViewById(R.id.url_save_cancel_button);
        url_save_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        priceTxt.setText("₹ " + String.valueOf(n)); // setting price value of screen

        Button savebtn = findViewById(R.id.url_save_save_button);
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(browse_act.this, url_final.class);
                intent.putExtra("price", String.valueOf(n));
                intent.putExtra("url", s);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onGroupClick(int pos) {
        ArrayList<String> gdata = mydb.get_groups();
        String s = gdata.get(pos);
        // Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(browse_act.this, group_universe.class);
        intent.putExtra("group name", s);
        intent.putExtra("prev_intent", "browse");
        startActivity(intent);
    }

    @Override
    public void onItemClick(int pos) {
        ArrayList<String> idata = mydb.get_all_names();
        String s = idata.get(pos);
        // Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(browse_act.this, report.class);
        intent.putExtra("name", s);
        intent.putExtra("prev_intent", "browse");
        startActivity(intent);
    }

    @Override
    public void onTaskComplete(String title, String url) {
        if (title.equals("null") == true) {
            Toast.makeText(this, "Cannot find page : ", Toast.LENGTH_SHORT).show();
            recreate();
        } else {
            int n = Integer.parseInt(title);
            // Toast.makeText(browse_act.this, "", Toast.LENGTH_SHORT).show();
            url_save_page(n, url);

            // v: view; n: price; url[0]: link
        }
    }

    // private AdSize getAdSize() {
    // // Determine the screen width (less decorations) to use for the ad width.
    // Display display = getWindowManager().getDefaultDisplay();
    // DisplayMetrics outMetrics = new DisplayMetrics();
    // display.getMetrics(outMetrics);
    //
    // float density = outMetrics.density;
    //
    // float adWidthPixels = adContainerView.getWidth();
    //
    // // If the ad hasn't been laid out, default to the full screen width.
    // if (adWidthPixels == 0) {
    // adWidthPixels = outMetrics.widthPixels;
    // }
    //
    // int adWidth = (int) (adWidthPixels / density);
    // return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this,
    // adWidth);
    // }
    //
    // private void loadBanner() {
    //
    // // Create a new ad view.
    // AdView adView = new AdView(browse_act.this);
    // adView.setAdSize(getAdSize());
    // adView.setAdUnitId("ca-app-pub-9103409493890880/5972454664");
    //
    // // Replace ad container with new ad view.
    // adContainerView.removeAllViews();
    // adContainerView.addView(adView);
    //
    // // Start loading the ad in the background.
    // AdRequest adRequest = new AdRequest.Builder().build();
    // adView.loadAd(adRequest);
    // }
}

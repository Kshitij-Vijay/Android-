package com.Xitij.pricescrapper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.Xitij.pricescrapper.Database.DatabaseHelper;
import com.Xitij.pricescrapper.Database.selfService;
import com.Xitij.pricescrapper.R;
import com.Xitij.pricescrapper.databinding.SettingsBinding;

public class settings extends AppCompatActivity {
    private SettingsBinding binding;
    DatabaseHelper mydb = new DatabaseHelper(settings.this);
    String prev_lay = null;
    String prev_intent = null;

    @Override
    public void onBackPressed() {
        if (prev_lay == "stores") {
            recreate();
            prev_lay = null;
        } else if (prev_intent.equals("browse")) {
            // Toast.makeText(this, "Browse page", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(settings.this, browse_act.class);
            intent.putExtra("prev_intent", "settings");
            startActivity(intent);
        } else if (prev_intent.equals("search")) {
            // Toast.makeText(this, "Search page", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(settings.this, search.class);
            intent.putExtra("prev_intent", "settings");
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        binding = SettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent2 = getIntent();
        prev_intent = intent2.getStringExtra("prev_intent");

        binding.settingsNavBar.setOnItemSelectedListener(item -> {
            final int v = item.getItemId();
            // if(v == R.id.homeMenu){
            //// Toast.makeText(this, "Going home", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(browse_act.this,home.class);
            // startActivity(intent);
            // }
            if (v == R.id.searchMenu) {
                // Toast.makeText(this, "Search page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(settings.this, search.class);
                intent.putExtra("prev_intent", "settings");
                startActivity(intent);
            } else if (v == R.id.browseMenu) {
                // Toast.makeText(this, "Browse page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(settings.this, browse_act.class);
                intent.putExtra("prev_intent", "settings");
                startActivity(intent);
            } else if (v == R.id.setingsMenu) {
                recreate();
            }
            return true;
        });

        ConstraintLayout settings_factory_reset_layout = findViewById(R.id.settings_factory_reset_layout);
        settings_factory_reset_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int REQUEST_CODE = 1;
                Intent intent = new Intent(settings.this, are_you_sure.class);
                intent.putExtra("vals", "Reset");
                startActivity(intent);
            }
        });

        ConstraintLayout settings_clear_layout = findViewById(R.id.settings_clear_layout);
        settings_clear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settings.this, are_you_sure.class);
                intent.putExtra("vals", "Clear");
                startActivity(intent);
            }
        });

        ConstraintLayout settings_Update_all = findViewById(R.id.settings_Update_all);
        settings_Update_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(settings.this, "Updated", Toast.LENGTH_SHORT).show();
                Intent serviceIntent = new Intent(settings.this, selfService.class);
                serviceIntent.putExtra("key", "a"); // Put your data here
                Log.d("SSSS", "Doing service to self");
                startService(serviceIntent);
            }
        });

        ConstraintLayout settings_def_refresh_rate = findViewById(R.id.settings_def_refresh_rate);
        settings_def_refresh_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settings.this, popupint.class);
                intent.putExtra("type", "freq");
                intent.putExtra("text", "Refresh Rate set");
                startActivity(intent);
            }
        });

        ConstraintLayout settings_def_notifinc = findViewById(R.id.settings_def_notifinc);
        settings_def_notifinc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settings.this, popupint.class);
                intent.putExtra("type", "notifinc");
                intent.putExtra("text", "Updated Default Increaments");
                startActivity(intent);
            }
        });

        ConstraintLayout settings_def_notifdec = findViewById(R.id.settings_def_notifdec);
        settings_def_notifdec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settings.this, popupint.class);
                intent.putExtra("type", "notifdec");
                intent.putExtra("text", "Updated Default Decrements");
                startActivity(intent);
            }
        });

        ConstraintLayout settings_stores = findViewById(R.id.settings_stores);
        settings_stores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.stores);
                prev_lay = "stores";
            }
        });

        ConstraintLayout settings_Version_and_help = findViewById(R.id.settings_Version_and_help);
        settings_Version_and_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.version_help);
                prev_lay = "stores";
            }
        });

    }
}

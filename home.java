package com.Xitij.pricescrapper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Xitij.pricescrapper.Database.DatabaseHelper;
import com.Xitij.pricescrapper.Models.string_int;
import com.Xitij.pricescrapper.Adapters.homeAdapter;
import com.Xitij.pricescrapper.Adapters.home_interface;
import com.Xitij.pricescrapper.Models.onlytwoStrings;
import com.Xitij.pricescrapper.R;
import com.Xitij.pricescrapper.databinding.HomeBinding;

import java.util.ArrayList;

public class home extends AppCompatActivity  implements home_interface {
    DatabaseHelper mydb = new DatabaseHelper(this);
    private HomeBinding binding;
    ArrayList<string_int> sit;
    ArrayList<onlytwoStrings> sir;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


        binding = HomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.homePageNavBar.setOnItemSelectedListener(item -> {
            final int v = item.getItemId();
//            if(v == R.id.homeMenu){
//                Toast.makeText(this, "Going home", Toast.LENGTH_SHORT).show();
//            }else
                if (v == R.id.searchMenu){
                Toast.makeText(this, "Search page", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(home.this,search.class);
                startActivity(intent);
            } else if (v == R.id.browseMenu) {
                Toast.makeText(this, "Browse page", Toast.LENGTH_SHORT).show();
                Intent intent =  new Intent(home.this,browse_act.class);
                startActivity(intent);
            }
            return true;
        });


        RecyclerView rv = findViewById(R.id.home_recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        sit = mydb.get_changes_table();
        sir = new ArrayList<onlytwoStrings>();
        for(string_int ele : sit){
            String statement;
            int price = ele.getPrice();
            String name = mydb.get_name_fromid(ele.get_colname());
            if(price > 0){
                statement = "price increased by "+price;
            }else {
                statement = "price decreased by "+price;
            }
            statement = statement + " on "+ele.getName();
            onlytwoStrings ti = new onlytwoStrings(name,statement);
            sir.add(ti);
        }
        homeAdapter ear = new homeAdapter(home.this,sir,this);
        rv.setAdapter(ear);
    }

    @Override
    public void onItemclick(int pos) {
        onlytwoStrings ty = sir.get(pos);
        String s = ty.d;
        Intent intent = new Intent(home.this,report.class);
        intent.putExtra("name",s);
        startActivity(intent);
    }
}

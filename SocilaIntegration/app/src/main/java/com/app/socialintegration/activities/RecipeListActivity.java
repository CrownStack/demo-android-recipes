package com.app.socialintegration.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.socialintegration.R;
import com.app.socialintegration.adapter.RecipeAdapter;

import java.util.ArrayList;

public class RecipeListActivity extends AppCompatActivity {

    RecyclerView recyclerViewRecipe;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        initView();
    }

    private void initView() {
        ArrayList<String> arrayListRecipe = new ArrayList<>();
        arrayListRecipe.add("SocialActivity");
        arrayListRecipe.add("GoogleMapActivity");

        recyclerViewRecipe = (RecyclerView) findViewById(R.id.recycle_view_recipe);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewRecipe.setLayoutManager(mLayoutManager);
        RecipeAdapter adapter = new RecipeAdapter(this, arrayListRecipe);
        recyclerViewRecipe.setAdapter(adapter);
    }
}
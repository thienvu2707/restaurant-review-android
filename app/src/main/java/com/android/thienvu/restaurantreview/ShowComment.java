package com.android.thienvu.restaurantreview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.thienvu.restaurantreview.Common.Common;
import com.android.thienvu.restaurantreview.Model.Rating;
import com.android.thienvu.restaurantreview.ViewHolder.ShowCommentViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowComment extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference ratingTable;

    SwipeRefreshLayout swipeRefreshLayout;

    FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder> adapter;

    String foodId = "";


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comment);


        database = FirebaseDatabase.getInstance();
        ratingTable = database.getReference("Rating");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerComment);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getIntent() != null)
                    foodId = getIntent().getStringExtra(Common.INTENT_FOOD_ID);
                if (!foodId.isEmpty() && foodId != null)
                {
                    adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(Rating.class, R.layout.show_comment_layout, ShowCommentViewHolder.class, ratingTable.orderByChild("foodId").equalTo(foodId)) {

                        @Override
                        protected void populateViewHolder(ShowCommentViewHolder viewHolder, Rating model, int position) {

                            viewHolder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            viewHolder.txtComment.setText(model.getComment());
                            viewHolder.txtUserName.setText(model.getUserName());

                        }
                    };

                    loadComment(foodId);
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(true);

                if (getIntent() != null)
                    foodId = getIntent().getStringExtra(Common.INTENT_FOOD_ID);
                if (!foodId.isEmpty() && foodId != null)
                {
                    adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(Rating.class, R.layout.show_comment_layout, ShowCommentViewHolder.class, ratingTable.orderByChild("foodId").equalTo(foodId)) {

                        @Override
                        protected void populateViewHolder(ShowCommentViewHolder viewHolder, Rating model, int position) {

                            viewHolder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            viewHolder.txtComment.setText(model.getComment());
                            viewHolder.txtUserName.setText(model.getUserName());

                        }
                    };

                    loadComment(foodId);
                }
            }
        });
    }

    private void loadComment(String foodId) {
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }
}

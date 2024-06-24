package com.example.recipehub.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipehub.R;
import com.example.recipehub.RecipeDetail;
import com.example.recipehub.models.Recipe;

import java.util.ArrayList;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    ArrayList<Recipe> recipes;

    public RecipeListAdapter(Context context, ArrayList<Recipe> recipes) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recipe_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.title.setText(recipe.getTitle());
        holder.description.setText(recipe.getDescription());
        holder.author.setText(recipe.getUser().getUsername());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView description;
        TextView author;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.recipeName);
            description = (TextView) itemView.findViewById(R.id.recipeDesc);
            author = (TextView) itemView.findViewById(R.id.recipeAuthor);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Recipe recipe = recipes.get(position);

            Intent intent = new Intent(context, RecipeDetail.class);
            intent.putExtra("recipeId", recipe.getId());
            context.startActivity(intent);
        }
    }

}

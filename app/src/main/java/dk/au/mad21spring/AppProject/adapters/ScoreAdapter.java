package dk.au.mad21spring.AppProject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad21spring.AppProject.R;
import dk.au.mad21spring.AppProject.model.Score;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {

    private List<Score> scoreArrayList = new ArrayList<>();
    private Context context;

    public ScoreAdapter(Context context) {
        //this.scoreArrayList = scoreArrayList;
        this.context = context;
    }

    public void setScores(List<Score> scores) {
        this.scoreArrayList = scores;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.score_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(scoreArrayList.get(position));
        holder.rowPlacement.setText(String.format("%d.", position + 1));
        holder.rowScore.setText(String.format("Score: %d", scoreArrayList.get(position).getScore()));
        holder.rowName.setText(scoreArrayList.get(position).getQuizzersName());
    }

    @Override
    public int getItemCount() {
        return scoreArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView rowPlacement, rowScore, rowName;

        ConstraintLayout constraintLayoutlayout;
        public ViewHolder(View itemView) {
            super(itemView);

            rowName = itemView.findViewById(R.id.rowName);
            rowPlacement = itemView.findViewById(R.id.rowPlacement);
            rowScore = itemView.findViewById(R.id.rowScore);
            constraintLayoutlayout = itemView.findViewById(R.id.ConstraintLayout);
            //score = itemView.findViewById(R.id.scoreTextView);
        }
    }
}

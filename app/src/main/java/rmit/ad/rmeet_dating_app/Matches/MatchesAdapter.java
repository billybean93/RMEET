package rmit.ad.rmeet_dating_app.Matches;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import rmit.ad.rmeet_dating_app.R;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolder>{
    private List<MatchesObject> matchesList;
    private Context context;


    public MatchesAdapter(List<MatchesObject> matchesList, Context context){
        this.matchesList = matchesList;
        this.context = context;
    }

    @Override
    public MatchesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches,
                null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.
                MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        MatchesViewHolder rcv = new MatchesViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(MatchesViewHolder holder, int position) {
        holder.mMatchId.setText(matchesList.get(position).getUserId());
        holder.mMatchName.setText(matchesList.get(position).getName());
        if(!matchesList.get(position).getProfileImageUrl().equals("default")){
            Glide.with(context).load(matchesList.get(position).
                    getProfileImageUrl()).into(holder.mMatchImage);
        }
    }

    @Override
    public int getItemCount() {
        return this.matchesList.size();
    }
}
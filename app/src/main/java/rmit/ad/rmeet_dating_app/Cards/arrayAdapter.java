package rmit.ad.rmeet_dating_app.Cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import rmit.ad.rmeet_dating_app.R;

public class arrayAdapter extends ArrayAdapter<cards> {
    Context context;

    public arrayAdapter(Context context, int resourceId, List<cards> items){
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        cards card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        ImageView image = convertView.findViewById(R.id.image);

        name.setText(card_item.getName());

        if (card_item.getprofileImageUrl().equals("default")) {
            image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(convertView.getContext())
                    .load(card_item.getprofileImageUrl())
                    .into(image);
        }


        return convertView;
    }
}

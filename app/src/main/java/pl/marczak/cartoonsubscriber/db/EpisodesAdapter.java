package pl.marczak.cartoonsubscriber.db;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.marczak.cartoonsubscriber.R;
import pl.marczak.cartoonsubscriber.left_tab.CurrentAnimeFragment;

/**
 * @author Lukasz Marczak
 * @since 12.06.16.
 */
public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.VH> {
    public static final String TAG = EpisodesAdapter.class.getSimpleName();
    List<Episode> dataSet;
    private CurrentAnimeFragment.Callbacks callbacks;

    public EpisodesAdapter() {
        this(new ArrayList<Episode>());
    }

    public EpisodesAdapter(List<Episode> dataSet) {
        this.dataSet = dataSet;
    }

    public void refreshDataSet(List<Episode> cartoons) {
        dataSet = cartoons;
        notifyItemRangeChanged(0, getItemCount());
        notifyDataSetChanged();
        Log.d(TAG, "refreshDataSet: ");

    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_news, null, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        final Episode episode = dataSet.get(position);
        holder.textView.setText(episode.title);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context ctx = holder.itemView.getContext();
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.url));
                //   Intent intent = new Intent(ctx, MoreNewsActivity.class);
                ///    intent.putExtra("URL", item.url);
                //    ctx.startActivity(intent);
                if (callbacks != null) callbacks.onEpisodeSelected(episode);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public void connectClickListener(CurrentAnimeFragment.Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView textView;
        TextView date;

        public VH(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.text);
            date = (TextView) v.findViewById(R.id.date);
        }
    }
}

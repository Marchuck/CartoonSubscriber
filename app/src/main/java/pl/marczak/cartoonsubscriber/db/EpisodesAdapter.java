package pl.marczak.cartoonsubscriber.db;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.marczak.cartoonsubscriber.R;
import pl.marczak.cartoonsubscriber.left_tab.CartoonEpisodesFragment;

/**
 * @author Lukasz Marczak
 * @since 12.06.16.
 */
public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.VH> {
    public static final String TAG = EpisodesAdapter.class.getSimpleName();
    List<Episode> dataSet = new ArrayList<>();
    List<Episode> selectedDataset = new ArrayList<>();
    private CartoonEpisodesFragment.Callbacks callbacks;
    private int recentSelection = -1;
    private boolean newEpisodeIndicator = false;

    public EpisodesAdapter() {
        this(new ArrayList<>());
    }

    public EpisodesAdapter(List<Episode> dataSet) {
        this.dataSet = dataSet;
        selectedDataset = dataSet;
    }

    public void refreshDataSet(List<Episode> cartoons) {
        selectedDataset.clear();
        dataSet.addAll(cartoons);
        selectedDataset.clear();
        selectedDataset.addAll(cartoons);
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
        final Episode episode = selectedDataset.get(position);
        if (newEpisodeIndicator && position == 0) {
            holder.newEpisode.setVisibility(View.VISIBLE);
            holder.newEpisode.setOnClickListener((view) -> {
                
                holder.newEpisode.setVisibility(View.GONE);
            });

        } else {
            holder.newEpisode.setVisibility(View.GONE);
        }
        holder.textView.setTextColor(recentSelection == position ? Color.RED : Color.BLACK);
        holder.textView.setText(episode.title);

        holder.itemView.setOnClickListener(v -> {
            recentSelection = position;
            notifyDataSetChanged();
            if (callbacks != null) callbacks.onEpisodeSelected(episode);
        });
    }

    @Override
    public int getItemCount() {
        return selectedDataset == null ? 0 : selectedDataset.size();
    }

    public void connectClickListener(CartoonEpisodesFragment.Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public void refreshAccordingToSeason(int newValue) {
        newEpisodeIndicator = false;
        Log.d(TAG, "refreshAccordingToSeason: " + newValue);
        String theQuery = "Season " + newValue;
        List<Episode> filteredBySeason = new ArrayList<>();
        Log.d(TAG, "datasetsize: " + dataSet.size());
        for (Episode ep : dataSet) {
            String[] splitted = ep.title.split(" ");
            for (int j = 0; j < splitted.length; j++) {
                if (splitted[j].equalsIgnoreCase("season"))
                    if (Integer.valueOf(splitted[j + 1]).compareTo(newValue) == 0)
                        filteredBySeason.add(ep);
            }

        }
        Log.d(TAG, "refreshAccordingToSeason: after filtering" + filteredBySeason.size());
        if (filteredBySeason.size() != 0)
            selectedDataset = filteredBySeason;
        notifyItemRangeChanged(0, getItemCount());
        notifyDataSetChanged();
    }

    public void notifyAboutNewEpisodes() {
        newEpisodeIndicator = true;
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView textView;
        ViewGroup newEpisode;

        public VH(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.text);
            newEpisode = (ViewGroup) v.findViewById(R.id.newEpisodeIndicator);
        }
    }
}

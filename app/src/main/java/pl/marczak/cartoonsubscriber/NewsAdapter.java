package pl.marczak.cartoonsubscriber;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 08.05.16.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.VH> {

    List<String> dataSet;

    public NewsAdapter() {
        if (dataSet == null) dataSet = new ArrayList< >();//{{add("no ");}};
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_news, null, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        final String item = dataSet.get(position);
        holder.textView.setText(item);
        holder.itemView.setOnClickListener(v -> {
            Context ctx = holder.itemView.getContext();
        });
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView textView;

        public VH(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.text);
        }
    }
}

package pl.marczak.cartoonsubscriber.db;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.marczak.cartoonsubscriber.R;
import pl.marczak.cartoonsubscriber.right_tab.RightNavigatorFragment;

/**
 * @author Lukasz Marczak
 * @since 12.06.16.
 */
public class CartoonAdapter extends RecyclerView.Adapter<CartoonAdapter.VH> {
    public static final String TAG = CartoonAdapter.class.getSimpleName();
    List<Cartoon> dataSet;
    private RightNavigatorFragment.Callbacks callbacks;

    public CartoonAdapter() {
        this(new ArrayList<>());
    }

    public CartoonAdapter(List<Cartoon> dataSet) {
        this.dataSet = dataSet;
    }

    public void setDataSet(List<Cartoon> cartoons) {
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
        final Cartoon cartoon = dataSet.get(position);
        holder.textView.setText(cartoon.title);

        holder.itemView.setOnClickListener(v -> {
            if (callbacks != null) callbacks.onRightItemSelected(cartoon);
        });
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public void connectClickListener(RightNavigatorFragment.Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public List<Cartoon> getData() {
        return dataSet;
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView textView;

        public VH(View v) {
            super(v);
            itemView.setClickable(true);
            textView = (TextView) v.findViewById(R.id.text);
        }
    }
}

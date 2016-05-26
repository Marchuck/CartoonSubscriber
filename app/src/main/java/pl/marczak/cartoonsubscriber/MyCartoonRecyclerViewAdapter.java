package pl.marczak.cartoonsubscriber;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.marczak.cartoonsubscriber.cartoononline.Cartoon;


public class MyCartoonRecyclerViewAdapter extends RecyclerView.Adapter<MyCartoonRecyclerViewAdapter.ViewHolder> {

    private List<Cartoon> mValues = new ArrayList<>();

    public void refresh(List<Cartoon> cartoons) {
        mValues = cartoons;
        notifyItemRangeChanged(0, getItemCount());
        notifyDataSetChanged();
    }

    CartoonFragment.OnListFragmentInteractionListener mListener;

    public MyCartoonRecyclerViewAdapter(CartoonFragment.OnListFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Cartoon cartoon = mValues.get(position);
        holder.mIdView.setText(cartoon.name);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onItemSelected(cartoon.url);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
       // public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.text);
//            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }
}

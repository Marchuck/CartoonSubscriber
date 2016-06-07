package pl.marczak.cartoonsubscriber;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class FakeCartoonAdapter extends RecyclerView.Adapter<FakeCartoonAdapter.ViewHolder> {

    public static final String TAG = FakeCartoonAdapter.class.getSimpleName();
    int itemsCount;
   private android.os.Handler animationsHandler = new Handler();
    float LOW = 0.3f;
    float HIGH = 0.7f;
    List<Runnable> runnables = new ArrayList<>();

    private String createFakeStr() {
        String res = "";
        String char1 = String.valueOf((char) 219);
        for (int j = 0; j < 7; j++) res += char1;
        return res;
    }

    public FakeCartoonAdapter(int items) {
        Log.d(TAG, "FakeCartoonAdapter: ");
        itemsCount = items;
        animationsHandler = new Handler();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        holder.mIdView.setText("??????????");
        holder.mIdView.setAlpha(LOW);
        loopFade(holder.mIdView, animationsHandler);
    }


    public void loopFade(final TextView tv, final Handler h) {
        Log.d(TAG, "loopFade: ");
        if (tv == null || h == null) return;
        tv.animate().alpha(HIGH).setDuration(300).start();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (tv == null) return;
                tv.animate().alpha(LOW).setDuration(300).start();
                if (tv != null && h != null) {
                    Runnable rr = new Runnable() {
                        @Override
                        public void run() {
                            loopFade(tv, h);
                        }
                    };
                    runnables.add(rr);
                    h.postDelayed(rr, 300);
                }
            }
        };
        runnables.add(r);
        h.postDelayed(r, 300);
    }

    @Override
    public int getItemCount() {
        return itemsCount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.text);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }

    public void clearCallbacks() {
        Log.d(TAG, "clearCallbacks: ");
        for (int j = 0; j < runnables.size(); j++) {
            animationsHandler.removeCallbacks(runnables.get(j));
            runnables.set(j, null);
        }
        runnables.clear();
    }
}

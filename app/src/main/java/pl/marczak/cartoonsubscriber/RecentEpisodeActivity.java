package pl.marczak.cartoonsubscriber;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class RecentEpisodeActivity extends AppCompatActivity {
    RecyclerView r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_episode);
        setTitle("Recent episodes");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        r = (RecyclerView) findViewById(R.id.recycler_view);
        r.setLayoutManager(new LinearLayoutManager(this));
        r.setAdapter(new NewsAdapter());
    }
}

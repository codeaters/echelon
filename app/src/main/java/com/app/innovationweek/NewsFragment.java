package com.app.innovationweek;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.innovationweek.adapter.NewsAdapter;
import com.app.innovationweek.callbacks.DaoOperationComplete;
import com.app.innovationweek.loader.NewsAsyncTaskLoader;
import com.app.innovationweek.model.News;
import com.app.innovationweek.model.dao.DaoSession;
import com.app.innovationweek.model.dao.NewsDao;
import com.app.innovationweek.util.NewsInsertTask;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * <p>
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment implements LoaderManager
        .LoaderCallbacks<List<News>>, DaoOperationComplete {

    private static final String TAG = NewsFragment.class.getSimpleName();

    @BindView(R.id.recycler_view_news)
    RecyclerView newsList;
    @BindView(R.id.progress)
    View progress;
    @BindView(R.id.progress_msg)
    TextView progressMsg;

    //Firebase variables
    private DatabaseReference mDatabaseReference;
    private ChildEventListener newsListener;
    private ValueEventListener newsValueEventListener;
    private NewsAdapter newsAdapter;
    private DaoSession daoSession;
    private List<DataSnapshot> dataSnapshots;
    private boolean allNewsFetched;

    {
        dataSnapshots = new ArrayList<>();
        newsListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                if (allNewsFetched) {
                    new NewsInsertTask(daoSession, NewsFragment.this).execute(dataSnapshot);
                    return;
                }

                if (dataSnapshot.getValue() != null) {
                    dataSnapshots.add(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                new NewsInsertTask(daoSession, NewsFragment.this).execute(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
                daoSession.getNewsDao().queryBuilder().where(NewsDao.Properties.NewsId.eq
                        (dataSnapshot.getKey())).buildDelete().executeDeleteWithoutDetachingEntities();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(getActivity().getApplicationContext(), "Failed to load news.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        newsValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //this is garenteed to be called las1t so we collect datanapshots in
                // ChildEventListener and insert them here giving a less fluctuating UI
                Log.d(TAG, "inserting news " + dataSnapshots.size());
                new NewsInsertTask(daoSession, NewsFragment.this).execute(dataSnapshots.toArray(new DataSnapshot[dataSnapshots.size()]));
                allNewsFetched = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "postComments:onCancelled", databaseError.toException());
            }
        };
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewsFragment.
     */

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("news");
        daoSession = ((EchelonApplication) getActivity().getApplication()).getDaoSession();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, rootView);

        newsAdapter = new NewsAdapter(new ArrayList<News>());

        newsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsList.setAdapter(newsAdapter);
        getActivity().getSupportLoaderManager().initLoader(1, null, this);
        mDatabaseReference.addListenerForSingleValueEvent(newsValueEventListener);
        return rootView;
    }

    @Override
    public void onStart() {
        mDatabaseReference.addChildEventListener(newsListener);
        super.onStart();
    }

    @Override
    public void onStop() {
        mDatabaseReference.removeEventListener(newsListener);
        super.onStop();
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        NewsAsyncTaskLoader loader = new NewsAsyncTaskLoader(getActivity().getApplicationContext());
        Log.d(TAG, "news loader created");
        return loader;
    }


    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> list) {
        Log.d(TAG, "News loaded " + list.size());
        newsAdapter.setNewsList(list);
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.w(TAG, "Loader has been reset");
    }

    private void showProgress(String msg) {
        if (progressMsg != null)
            progressMsg.setText(msg == null || msg.isEmpty() ? getString(R.string.loading_news) : msg);
        if (progress != null) progress.setVisibility(View.VISIBLE);
        if (newsList != null) newsList.setVisibility(View.GONE);
    }

    private void hideProgress() {
        if (progress != null) progress.setVisibility(View.GONE);
        if (newsList != null) newsList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDaoOperationComplete(Object object) {
        //new news item here from db
        if (isAdded()) {
            if (object == null) {
                getActivity().getSupportLoaderManager().getLoader(1).forceLoad();
                return;
            }
            News news = (News) object;
            int index = newsAdapter.getNewsList().indexOf(news);
            if (index > -1) {
                Log.d(TAG,"old news at"+index);
                newsAdapter.getNewsList().set(index, news);
                newsAdapter.notifyItemChanged(index);
            } else {
                //new news item, add it to top
                Log.d(TAG,"new news inserting");
                newsAdapter.addNewsAtTop(news);
            }
        }
    }
}

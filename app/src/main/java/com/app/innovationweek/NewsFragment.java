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
import android.widget.Toast;

import com.app.innovationweek.adapter.NewsAdapter;
import com.app.innovationweek.loader.NewsAsyncTaskLoader;
import com.app.innovationweek.model.News;
import com.app.innovationweek.model.dao.NewsDao;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        .LoaderCallbacks<List<News>> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = NewsFragment.class.getSimpleName();
    @BindView(R.id.recycler_view_news)
    RecyclerView recyclerView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //Firebase variables
    private DatabaseReference mDatabaseReference;
    private ChildEventListener newsListener;
    private NewsAdapter newsAdapter;
    private NewsDao newsDao;

    {
        newsListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                News news = dataSnapshot.getValue(News.class);
                news.setNewsId(dataSnapshot.getKey());
                NewsAdapter newsAdapter = ((NewsAdapter) recyclerView.getAdapter());
                if (!newsAdapter.getNewsList().contains(news)) {
                    newsAdapter.getNewsList().add(0, news);
                    newsAdapter.notifyItemInserted(0);
                    newsDao.insertOrReplace(news);
                }

                Log.d(TAG, news.toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                News news = dataSnapshot.getValue(News.class);
                news.setNewsId(dataSnapshot.getKey());
                NewsAdapter newsAdapter = ((NewsAdapter) recyclerView.getAdapter());
                int index = newsAdapter.getNewsList().indexOf(news);
                if (index > -1) {
                    newsAdapter.getNewsList().remove(index);
                    newsAdapter.getNewsList().add(index, news);
                    newsAdapter.notifyItemInserted(index);
                    newsDao.insertOrReplace(news);
                } else
                    Log.w(TAG, "News Item Changed but not updated in the RecyclerView");

                Log.d(TAG, news.toString());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(getActivity().getApplicationContext(), "Failed to load news.",
                        Toast.LENGTH_SHORT).show();
            }
        };
    }


    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("news/");
        newsDao = ((EchelonApplication) getActivity().getApplication()).getDaoSession().getNewsDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, rootView);

        newsAdapter = new NewsAdapter(getActivity().getApplicationContext(), new ArrayList<News>());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(newsAdapter);

        getActivity().getSupportLoaderManager().initLoader(1, null, this).forceLoad();

        return rootView;
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
        Log.d(TAG, "loader created");
        return loader;
    }


    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> list) {
        Log.d(TAG, "News loaded");
        NewsAdapter newsAdapter = (NewsAdapter) recyclerView.getAdapter();
        newsAdapter.setNewsList(list);
        newsAdapter.notifyDataSetChanged();
        mDatabaseReference.addChildEventListener(newsListener);
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
}

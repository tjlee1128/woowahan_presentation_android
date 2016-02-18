package com.baemin.woowahan_presentation_android.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baemin.woowahan_presentation_android.R;

import java.util.ArrayList;
import java.util.List;

import co.moonmonkeylabs.realmsearchview.RealmSearchView;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchPresenterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchPresenterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchPresenterFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private List<String> searchedList;
    private Realm realm;
    private RealmSearchView realmSearchView;

    public SearchPresenterFragment() {
        // Required empty public constructor
    }

    public static SearchPresenterFragment newInstance(List<String> list) {
        SearchPresenterFragment fragment = new SearchPresenterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_presenter, container, false);
        realmSearchView = (RealmSearchView) view.findViewById(R.id.fragment_search_presenter_rsv);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getInstance(getActivity());

        UserRecyclerViewAdapter adapter = new UserRecyclerViewAdapter(getActivity(), realm, "fullname");
        realmSearchView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (realm != null) {
            realm.close();
            realm = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

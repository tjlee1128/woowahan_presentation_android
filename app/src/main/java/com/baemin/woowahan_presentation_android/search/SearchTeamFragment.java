package com.baemin.woowahan_presentation_android.search;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baemin.woowahan_presentation_android.R;
import com.baemin.woowahan_presentation_android.model.TeamModel;
import com.baemin.woowahan_presentation_android.presentation.PresentationsActivity;
import com.baemin.woowahan_presentation_android.util.Constants;
import com.baemin.woowahan_presentation_android.util.PreferencesManager;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchTeamFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchTeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchTeamFragment extends Fragment {

    private static final String ARG_TEAM_MODEL = "ARG_TEAM_MODEL";

    private ListView teamsListView;
    private SearchTeamsAdapter searchTeamsAdapter;

    public SearchTeamFragment() {
        // Required empty public constructor
    }

    public static SearchTeamFragment newInstance() {
        SearchTeamFragment fragment = new SearchTeamFragment();
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
        View view = inflater.inflate(R.layout.fragment_search_team, container, false);
        teamsListView = (ListView) view.findViewById(R.id.fragment_search_team_lv);
        teamsListView.setAdapter(new SearchTeamsAdapter(getActivity(), PreferencesManager.getInstance().getTeams()));
        teamsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PresentationsActivity.class);
                intent.putExtra(Constants.EXTRA_TEAM_ID, PreferencesManager.getInstance().getTeams().get(position).getId());
                intent.putExtra(Constants.EXTRA_TEAM_NAME, PreferencesManager.getInstance().getTeams().get(position).getName());
                intent.putExtra(Constants.EXTRA_PRESENTATION_COME_IN, "team_id_presentations");
                startActivity(intent);
            }
        });

        return view;
    }
}

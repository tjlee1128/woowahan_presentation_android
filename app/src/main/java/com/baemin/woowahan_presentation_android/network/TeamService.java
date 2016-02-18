package com.baemin.woowahan_presentation_android.network;

import com.baemin.woowahan_presentation_android.model.PresentationsModel;
import com.baemin.woowahan_presentation_android.model.TeamModel;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by leetaejun on 2016. 2. 14..
 */
public interface TeamService {
    @GET("api/teams.json")
    Call<List<TeamModel>> loadTeams();

    @GET("api/teams/presentations.json")
    Call<PresentationsModel> loadPresentationByTeam(@Query("team_id") Integer team_id);
}

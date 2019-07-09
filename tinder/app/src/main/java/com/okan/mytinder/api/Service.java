package com.okan.mytinder.api;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {
    @GET("/teams/teams.json")
    Call<JsonArray> readTeamArray();

}

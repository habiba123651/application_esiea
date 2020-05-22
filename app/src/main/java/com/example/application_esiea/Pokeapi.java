package com.example.application_esiea;

import android.telecom.Call;

import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Pokeapi {
    @GET("/api/v2/pokemon")
    Call<RestPokemonResponse> GETPokemonResponse();

    @GET("/api/v2/ability")
    Call<RestPokemonResponse> GETAbility();

    retrofit2.Call<RestPokemonResponse> getPokemonResponse();
}

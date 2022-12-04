package com.example.merqueapp.pokeapiService;

import com.example.merqueapp.models.PokemonRespuesta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PokeapiService {

    @GET("pokemon") //obtener la info de la url
    Call<PokemonRespuesta> obtenerListaPokemon(@Query("limit")int limit,@Query("offset")int offset);
}
//obtener la lista pokemon para que sea parametrizable, son parametros por medio query y el nombre exacto de la api

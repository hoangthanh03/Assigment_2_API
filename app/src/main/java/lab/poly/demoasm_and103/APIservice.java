package lab.poly.demoasm_and103;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIservice {
    String DOMAIN = "http://10.24.25.122:3000/";
    @GET("api/fruits")
    Call<List<fruitModel>> getfruitList();

    @POST("api/fruits")
    Call<fruitModel> addFruit(@Body fruitModel fruitModel);

    @PUT("/api/fruits/{id}")
    Call<fruitModel> updateFruit(@Path("id") String id, @Body fruitModel fruitModel);

    @DELETE("/api/fruits/{id}")
    Call<Void> deleteFruit(@Path("id") String id);
}


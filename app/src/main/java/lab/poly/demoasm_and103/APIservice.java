package lab.poly.demoasm_and103;

import java.util.List;

import lab.poly.demoasm_and103.models.Fruit;
import lab.poly.demoasm_and103.models.Users;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIservice {
String BASE_URL = "http://192.168.1.75:3000/";
    @GET("/api/fruits")
    Call<List<Fruit>> getfruitList();

    @GET("api/search")
    Call<List<Fruit>> searchFruit(@Query("key") String key);
     @GET("/api/sxepTang")
     Call<List<Fruit>> sxepTang();

     @GET("/api/sxepGiam")
     Call<List<Fruit>> sxepGiam();

    @Multipart
    @POST("api/fruits")
    Call<Fruit> addFruit(@Part("name") RequestBody name, @Part("price") RequestBody price, @Part("origin") RequestBody origin, @Part MultipartBody.Part image, @Part("quantity") RequestBody quantity);

    @Multipart
    @PUT("api/fruits/{id}")
    Call<Fruit> updateFruit(@Path("id") String id, @Part("name") RequestBody name, @Part("price") RequestBody price, @Part("origin") RequestBody origin, @Part MultipartBody.Part image, @Part("quantity") RequestBody quantity);

    @DELETE("api/fruits/{id}")
    Call<Void> deleteFruit(@Path("id") String id);

    @Multipart
    @POST("api/register")
    Call<Users> register(@Part("username") RequestBody username, @Part("password") RequestBody password, @Part("email") RequestBody email, @Part("name") RequestBody name, @Part MultipartBody.Part avartar);

    @POST("api/login")
    Call<Users> login(@Body Users users);
}


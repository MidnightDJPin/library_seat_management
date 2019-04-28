package pin.com.libraryseatmanagementsystem.Net;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import pin.com.libraryseatmanagementsystem.Bean.Order;
import pin.com.libraryseatmanagementsystem.Bean.Reader;
import pin.com.libraryseatmanagementsystem.Bean.Seat;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class NetworkConnection {
    private OkHttpClient bulid;
    private Retrofit retrofit;
    private static NetworkConnection instance;

    private NetworkConnection() {
        bulid = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.18.158.85:8080/Servlet/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(bulid)
                .build();
    }

    public static NetworkConnection getInstance() {
        if (instance == null)
            instance = new NetworkConnection();
        return instance;
    }

    public interface LibraryService {
        @POST("Register")
        Call<Reader> register(@Body RequestBody requestBody);

        @POST("Login")
        Call<Reader> login(@Body RequestBody requestBody);

        @GET("SeatServlet")
        Call<List<Seat>> getSeats();

        @POST("UpdateReader")
        Call<Reader> updateReader(@Body RequestBody requestBody);

        @GET("OrderServlet")
        Call<List<Order>> getOrders(@Query("rid") String rid, @Query("sid") String sid, @Query("time") String time);
    }

    public Call<List<Seat>> getSeats() {
        LibraryService service = retrofit.create(LibraryService.class);
        return service.getSeats();
    }

    public Call<Reader> login(String json) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        LibraryService service = retrofit.create(LibraryService.class);
        return service.login(requestBody);
    }

    public Call<Reader> register(String json) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        LibraryService service = retrofit.create(LibraryService.class);
        return service.register(requestBody);
    }

    public Call<Reader> updateReader(String json) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        LibraryService service = retrofit.create(LibraryService.class);
        return service.updateReader(requestBody);
    }

    public Call<List<Order>> getOrders(String rid, String sid, String time) {
        LibraryService service = retrofit.create(LibraryService.class);
        return service.getOrders(rid, sid, time);
    }
}

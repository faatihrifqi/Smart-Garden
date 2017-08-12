package erwin.com.smartgarden.requests;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SmartGardenAPI {

    @GET("/kelembaban")
    Call<List<Kelembaban>> getKelembaban();

}

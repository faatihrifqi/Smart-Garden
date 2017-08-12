package erwin.com.smartgarden;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import erwin.com.smartgarden.requests.Kelembaban;
import erwin.com.smartgarden.requests.SmartGardenAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailData extends AppCompatActivity {

    private TextView tv_id, tv_tanggal, tv_kelembaban;

    private static String baseUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data);

        getSupportActionBar().setTitle("Detail Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_id = (TextView) findViewById(R.id.tv_id);
        tv_tanggal = (TextView) findViewById(R.id.tv_tanggal);
        tv_kelembaban = (TextView) findViewById(R.id.tv_kelembaban);

        tv_id.setText("");
        tv_tanggal.setText("");
        tv_kelembaban.setText("");

        retrieveData();

    }

    private void retrieveData(){

        final ArrayList<Kelembaban> kelembabanArrayList = new ArrayList<>();

        HashMap<String, String> user = new SessionManager(getApplicationContext()).getUserDetails();
        baseUrl = user.get(SessionManager.SERVER);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SmartGardenAPI smartGardenAPI = retrofit.create(SmartGardenAPI.class);
        Call<List<Kelembaban>> call = smartGardenAPI.getKelembaban();

        call.enqueue(new Callback<List<Kelembaban>>() {

            @Override
            public void onResponse(Call<List<Kelembaban>> call, Response<List<Kelembaban>> response) {
                List<Kelembaban> kelembabanList = response.body();

                for(Kelembaban kelembaban : kelembabanList){
                    kelembabanArrayList.add(kelembaban);
                }

                if(kelembabanArrayList.size() > 0){
                    for(Kelembaban kelembaban : kelembabanArrayList){
                        tv_id.append(Integer.toString(kelembaban.getId()) + "\n");
                        tv_tanggal.append(parsingTanggal(kelembaban.getTanggal()) + "\n");
                        tv_kelembaban.append(kelembaban.getNilai() + "\n");
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "No data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Kelembaban>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR :\n" + t.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String parsingTanggal(String tanggal){

        //2017-05-06T08:05:14.000Z
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        Date date = null;
        try {
            date = sdf.parse(tanggal.replaceAll("Z$", "+0000"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat writeDate = new SimpleDateFormat("dd MMM HH:mm:ss");

        return writeDate.format(date);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        DetailData.this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home){
            onBackPressed();
        }
        return true;
    }
}

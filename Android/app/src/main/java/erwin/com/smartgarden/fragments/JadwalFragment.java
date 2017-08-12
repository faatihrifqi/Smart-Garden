package erwin.com.smartgarden.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import erwin.com.smartgarden.R;
import erwin.com.smartgarden.SessionManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JadwalFragment extends Fragment {

    private Button btn_ubahJadwal;
    private TimePicker timePicker;
    private TextView tv_jadwalPenyiraman;

    private int jam_jadwalBaru = 0;
    private int menit_jadwalBaru = 0;

    public JadwalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jadwal, container, false);

        btn_ubahJadwal = (Button) view.findViewById(R.id.btn_ubahJadwal);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        tv_jadwalPenyiraman = (TextView) view.findViewById(R.id.tv_jadwalPenyiraman);

        HashMap<String, String> user = new SessionManager(getActivity().getApplicationContext()).getUserDetails();

        String jam = user.get(SessionManager.JAM_JADWALSIRAM);
        String menit = user.get(SessionManager.MENIT_JADWALSIRAM);

        tampilkanJam(Integer.parseInt(jam), Integer.parseInt(menit));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(Integer.parseInt(jam));
            timePicker.setMinute(Integer.parseInt(menit));
        } else {
            timePicker.setCurrentHour(Integer.parseInt(jam));
            timePicker.setCurrentMinute(Integer.parseInt(menit));
        }

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                tampilkanJam(hourOfDay, minute);

                jam_jadwalBaru = hourOfDay;
                menit_jadwalBaru = minute;
            }
        });

        btn_ubahJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //http://localhost:3000/api/jadwal?jam={7}&menit={30}

                HashMap<String, String> user = new SessionManager(getActivity().getApplicationContext()).getUserDetails();
                String baseUrl = user.get(SessionManager.SERVER);

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(baseUrl + "api/jadwal?jam=" + Integer.toString(jam_jadwalBaru) + "&menit=" + Integer.toString(menit_jadwalBaru))
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SessionManager.MYPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(SessionManager.JAM_JADWALSIRAM, Integer.toString(jam_jadwalBaru));
                        editor.putString(SessionManager.MENIT_JADWALSIRAM, Integer.toString(menit_jadwalBaru));
                        editor.commit();

                        tampilkanToast("Jadwal berhasil di ubah");
                    }

                    @Override
                    public void onFailure(Call call, final IOException e) {
                        tampilkanToast("Gagal mengubah jadwal :\n" + e.getMessage().toString());
                    }
                });
            }
        });

        return view;
    }

    private void tampilkanToast(final String pesan){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(), pesan, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void tampilkanJam(int newJam, int newMenit){

        String jam;
        String menit;

        if(newJam < 10){
            jam = "0" + Integer.toString(newJam);
        }else {
            jam = Integer.toString(newJam);
        }

        if(newMenit < 10){
            menit = "0" + Integer.toString(newMenit);
        }else {
            menit = Integer.toString(newMenit);
        }

        tv_jadwalPenyiraman.setText(jam + ":" + menit);
    }
}


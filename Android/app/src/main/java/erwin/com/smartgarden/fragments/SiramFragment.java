package erwin.com.smartgarden.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
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

public class SiramFragment extends Fragment {

    private Switch switch_tanaman1;

    public SiramFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_siram, container, false);

        switch_tanaman1 = (Switch) view.findViewById(R.id.switch_tanaman1);

        switch_tanaman1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //http://localhost:3000/api/siram?status=2

                    HashMap<String, String> user = new SessionManager(getActivity().getApplicationContext()).getUserDetails();
                    String baseUrl = user.get(SessionManager.SERVER);

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(baseUrl + "api/siram?status=2")
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            tampilkanToast("Tanaman 1 di siram");
                        }

                        @Override
                        public void onFailure(Call call, final IOException e) {
                            tampilkanToast("Gagal menyiram :\n" + e.getMessage().toString());
                        }
                    });
                }else {
                    //http://localhost:3000/api/siram?status=0

                    HashMap<String, String> user = new SessionManager(getActivity().getApplicationContext()).getUserDetails();
                    String baseUrl = user.get(SessionManager.SERVER);

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(baseUrl + "api/siram?status=0")
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            tampilkanToast("Tanaman 1 berhenti di siram");
                        }

                        @Override
                        public void onFailure(Call call, final IOException e) {
                            tampilkanToast("Gagal berhenti menyiram :\n" + e.getMessage().toString());
                        }
                    });
                }
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

}

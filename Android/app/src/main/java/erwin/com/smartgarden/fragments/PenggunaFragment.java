package erwin.com.smartgarden.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import erwin.com.smartgarden.R;
import erwin.com.smartgarden.SessionManager;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PenggunaFragment extends Fragment {

    SharedPreferences sharedpreferences;

    private EditText inputName, inputEmail, inputPassword, inputTelp, inputServer;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword, inputLayoutTelp, inputLayoutServer;
    private Button btn_simpanPengguna;

    public PenggunaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pengguna, container, false);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        sharedpreferences = getActivity().getSharedPreferences(SessionManager.MYPREFERENCES, Context.MODE_PRIVATE);

        inputLayoutName = (TextInputLayout) view.findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) view.findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) view.findViewById(R.id.input_layout_password);
        inputLayoutTelp = (TextInputLayout) view.findViewById(R.id.input_layout_telp);
        inputLayoutServer = (TextInputLayout) view.findViewById(R.id.input_layout_server);
        inputName = (EditText) view.findViewById(R.id.input_name);
        inputEmail = (EditText) view.findViewById(R.id.input_email);
        inputPassword = (EditText) view.findViewById(R.id.input_password);
        inputTelp = (EditText) view.findViewById(R.id.input_telp);
        inputServer = (EditText) view.findViewById(R.id.input_server);
        btn_simpanPengguna = (Button) view.findViewById(R.id.btn_simpanPengguna);

        // get user data from session
        HashMap<String, String> user = new SessionManager(getActivity().getApplicationContext()).getUserDetails();
        String name = user.get(SessionManager.NAME);
        String password = user.get(SessionManager.PASSWORD);
        String email = user.get(SessionManager.EMAIL);
        String telp = user.get(SessionManager.TELP);
        String server = user.get(SessionManager.SERVER);

        inputName.setText(name);
        inputPassword.setText(password);
        inputEmail.setText(email);
        inputTelp.setText(telp);
        inputServer.setText(server);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        inputTelp.addTextChangedListener(new MyTextWatcher(inputTelp));
        inputServer.addTextChangedListener(new MyTextWatcher(inputServer));

        btn_simpanPengguna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

        return view;
    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        if (!validateTelp()) {
            return;
        }

        if (!validateServer()) {
            return;
        }

        String name = inputName.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String telp = inputTelp.getText().toString().trim();
        String server = inputServer.getText().toString().trim();

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(SessionManager.NAME, name);
        editor.putString(SessionManager.PASSWORD, password);
        editor.putString(SessionManager.EMAIL, email);
        editor.putString(SessionManager.TELP, telp);
        editor.putString(SessionManager.SERVER, server);
        editor.commit();

        Toast.makeText(getActivity().getApplicationContext(), "Perubahan telah di simpan!", Toast.LENGTH_SHORT).show();
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateTelp() {
        if (inputTelp.getText().toString().trim().isEmpty()) {
            inputLayoutTelp.setError(getString(R.string.err_msg_telp));
            requestFocus(inputTelp);
            return false;
        } else {
            inputLayoutTelp.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateServer() {
        if (inputServer.getText().toString().trim().isEmpty()) {
            inputLayoutServer.setError(getString(R.string.err_msg_server));
            requestFocus(inputServer);
            return false;
        } else {
            inputLayoutServer.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
                case R.id.input_telp:
                    validateTelp();
                    break;
                case R.id.input_server:
                    validateTelp();
                    break;
            }
        }
    }

}

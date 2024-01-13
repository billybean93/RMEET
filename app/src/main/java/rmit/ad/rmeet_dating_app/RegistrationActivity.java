package rmit.ad.rmeet_dating_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrationActivity extends AppCompatActivity {

    private EditText email, password;
    private TextView btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPass);
        btn = findViewById(R.id.register);

    }
}
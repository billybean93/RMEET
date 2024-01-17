package rmit.ad.rmeet_dating_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChoosingLoginRegistrationActivity extends AppCompatActivity {

    private TextView loginBtn, registrationBtn;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Override
    public void onStart(){
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(user != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_login_registration);

        loginBtn = findViewById(R.id.login);
        registrationBtn = findViewById(R.id.registration);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoosingLoginRegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoosingLoginRegistrationActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}
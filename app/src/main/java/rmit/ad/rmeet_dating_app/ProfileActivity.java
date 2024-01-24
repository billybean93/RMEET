package rmit.ad.rmeet_dating_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import rmit.ad.rmeet_dating_app.Matches.MatchesActivity;

public class ProfileActivity extends AppCompatActivity {

    private BottomNavigationView nav1;
    private TextView proName, proAge, proJob, proPhone;
    private String userId, name, age, phone, job;
    private ImageView ProfileImg, settingBtn;

    private DatabaseReference mCustomerDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String userSex = Objects.requireNonNull(getIntent().getExtras()).getString("userSex");

        nav1 = findViewById(R.id.nav1);

        nav1.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    intent.putExtra("userSex", userSex);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.profile) {
                    return true;
                } else if (id == R.id.matches) {
                    Intent intent = new Intent(ProfileActivity.this, MatchesActivity.class);
                    intent.putExtra("userSex", userSex);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        settingBtn = findViewById(R.id.setting);
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                intent.putExtra("userSex", userSex);
                startActivity(intent);
            }
        });

        proName = findViewById(R.id.profileName);
        proAge = findViewById(R.id.profileAge);
        proJob = findViewById(R.id.profileJob);
        proPhone = findViewById(R.id.profilePhone);
        ProfileImg = findViewById(R.id.profileImage);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userSex).child(userId);
        getUserInfo();
    }

    private void getUserInfo() {
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if (map.get("name") != null) {
                        name = map.get("name").toString();
                        proName.setText(name);
                    }
                    if (map.get("phone") != null) {
                        phone = map.get("phone").toString();
                        proPhone.setText(phone);
                    }
                    if (map.get("age") != null) {
                        age = map.get("age").toString();
                        proAge.setText(age);
                    }
                    if (map.get("certificate") != null) {
                        job = map.get("certificate").toString();
                        proJob.setText(job);
                    }
                    //Glide.clear(ProfileImg);
                    if (map.get("profileImageUrl") != null) {
                        String profileImageUrl = map.get("profileImageUrl").toString();
                        if (profileImageUrl.equals("default")) {
                            ProfileImg.setImageResource(R.drawable.baseline_person_24);
                        } else {
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference(profileImageUrl);
                            try {
                                File localfile = File.createTempFile("tempfile", ".jpg");
                                storageReference.getFile(localfile)
                                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                                ProfileImg.setImageBitmap(bitmap);
//                                                        Glide.with(getApplication())
//                                                                .load(profileImageUrl)
//                                                                .into(ProfileImg);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
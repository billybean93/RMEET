package rmit.ad.rmeet_dating_app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SettingsActivity extends AppCompatActivity {
    private EditText nameField, phoneField, ageField, certificateField, educationyearField;
    private TextView confirm, back;
    private ImageView ProfileImg;
    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;
    private String userId, name, phone, age, certificate, educationyear, profileImageUrl;
    private Uri resultUri;

    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String userSex = Objects.requireNonNull(getIntent().getExtras()).getString("userSex");
        nameField = findViewById(R.id.name);
        phoneField = findViewById(R.id.phone);
        ageField = findViewById(R.id.age);
        certificateField = findViewById(R.id.certificate);
        educationyearField = findViewById(R.id.educationyear);

        ProfileImg = findViewById(R.id.profileImg);
        confirm = findViewById(R.id.confirmBtn);
        back = findViewById(R.id.backBtn);
        mAuth = FirebaseAuth.getInstance();
        userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userSex).child(userId);
        getUserInfo();

        registerResult();
        ProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    private void getUserInfo() {
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if (map.get("name") != null) {
                        name = map.get("name").toString();
                        nameField.setText(name);
                    }
                    if (map.get("phone") != null) {
                        phone = map.get("phone").toString();
                        phoneField.setText(phone);
                    }
                    if (map.get("age") != null) {
                        age = map.get("age").toString();
                        ageField.setText(age);
                    }
                    if (map.get("certificate") != null) {
                        certificate = map.get("certificate").toString();
                        certificateField.setText(certificate);
                    }
                    if (map.get("educationyear") != null) {
                        educationyear = map.get("educationyear").toString();
                        educationyearField.setText(educationyear);
                    }
                    //Glide.clear(ProfileImg);
                    if (map.get("profileImageUrl") != null) {
                        profileImageUrl = map.get("profileImageUrl").toString();
                        if (profileImageUrl.equals("default")) {
                            ProfileImg.setImageResource(R.mipmap.ic_launcher);
                        } else {
                            Glide.with(getApplication())
                                    .load(profileImageUrl)
                                    .into(ProfileImg);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void saveUserInformation() {
        name = nameField.getText().toString();
        phone = phoneField.getText().toString();
        age = ageField.getText().toString();
        certificate = certificateField.getText().toString();
        educationyear = educationyearField.getText().toString();

        mCustomerDatabase.child("name").setValue(name);
        mCustomerDatabase.child("phone").setValue(phone);
        mCustomerDatabase.child("age").setValue(age);
        mCustomerDatabase.child("certificate").setValue(certificate);
        mCustomerDatabase.child("educationyear").setValue(educationyear);

        if (resultUri != null) {
            String filepart = "image/" + UUID.randomUUID().toString();
            StorageReference reference = FirebaseStorage.getInstance().getReference().child(filepart);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = reference.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                    Map userInfo = new HashMap();
                    userInfo.put("profileImageUrl", downloadUrl.toString());
                    mCustomerDatabase.updateChildren(userInfo);
                    finish();
                    return;
                }
            });
        } else {
            finish();
        }
    }

    private void registerResult() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getData() != null) {
                            Uri urlimg = o.getData().getData();
                            resultUri = urlimg;
                            ProfileImg.setImageURI(urlimg);
                        } else {
                            Toast.makeText(SettingsActivity.this, "No Image chosen", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}
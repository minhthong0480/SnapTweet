package rmit.ad.snaptweet;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;

import rmit.ad.snaptweet.Model.User;

public class EditProfileActivity extends AppCompatActivity {

    ImageView close, image_profile;
    TextView save, tv_change;
    EditText fullname, username, bio;

    FirebaseUser firebaseUser;

    private Uri mImageUri;
    private StorageTask uploadTask;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        close = findViewById(R.id.close);
        image_profile = findViewById(R.id.image_profile);
        save = findViewById(R.id.save);
        tv_change = findViewById(R.id.tv_change);
        fullname = findViewById(R.id.fullname);
        username = findViewById(R.id.username);
        bio = findViewById(R.id.bio);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference("uploads");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                fullname.setText(user.getFullname());
                username.setText(user.getUsername());
                bio.setText(user.getBio());
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(fullname.getText().toString(),
                        username.getText().toString(),
                        bio.getText().toString());
                uploadImage();
            }

        });
    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                launchImageCropper(imageUri);
            }
        }
    });

    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), result -> {
        if (result.isSuccessful()) {
            mImageUri = result.getUriContent();
            Bitmap cropped = BitmapFactory.decodeFile(result.getUriFilePath(getApplicationContext(), true));
            saveCroppedImage(cropped);
        }
    });

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getImage.launch(intent);
    }

    private void launchImageCropper(Uri uri) {
        CropImageOptions cropImageOptions = new CropImageOptions();
        cropImageOptions.imageSourceIncludeGallery = true;
        cropImageOptions.imageSourceIncludeCamera = true;
        cropImageOptions.cropShape = CropImageView.CropShape.OVAL;
        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(uri, cropImageOptions);

        // Launch the image cropper
        cropImage.launch(cropImageContractOptions);
    }

    private void saveCroppedImage(Bitmap bitmap) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/Cropped Profile Images");

        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        // Generate a unique file name
        String imageName = "Image_" + new Date().getTime() + ".jpg";

        File file = new File(myDir, imageName);
        if (file.exists()) file.delete();

        try {
            // Save the Bitmap to the file
            OutputStream outputStream;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                outputStream = Files.newOutputStream(file.toPath());
            } else {
                outputStream = new FileOutputStream(file);
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Add the image to the MediaStore
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            // Trigger a media scan to update the gallery
            MediaScannerConnection.scanFile(getApplicationContext(), new String[]{file.getAbsolutePath()}, null, null);
            showSuccessMessage();
        } catch (Exception e) {
            showFailureMessage();
        }
        image_profile.setImageBitmap(bitmap);
    }
    private void showFailureMessage() {
        Toast.makeText(getApplicationContext(), "Cropped image not saved something went wrong", Toast.LENGTH_LONG).show();
    }

    private void showSuccessMessage() {
        Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_LONG).show();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void updateProfile(String fullname, String username, String bio) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("fullname", fullname);
        hashMap.put("username", username);
        hashMap.put("bio", bio);

        reference.updateChildren(hashMap);
    }

    private void uploadImage() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (mImageUri != null) {
            StorageReference filereference = storageRef.child(System.currentTimeMillis()
            +"."+getFileExtension(mImageUri));

            uploadTask = filereference.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String myUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imageurl", ""+myUrl);

                        reference.updateChildren(hashMap);
                        pd.dismiss();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

}
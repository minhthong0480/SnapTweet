package rmit.ad.snaptweet;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.File;
import java.io.IOException;

public class PostActivity extends AppCompatActivity {

    Uri imageUri;
    String myUrl = "";
    StorageTask uploadTask;
    StorageReference storageReference;

    ImageView close, image_added;
    TextView post;
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        close = findViewById(R.id.close);
        image_added = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);

        storageReference = FirebaseStorage.getInstance().getReference("posts");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });

        image_added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the image picker when the user clicks on the image view
                pickImage();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    // Start the crop image activity
                    startCropImage(selectedImageUri);
                }
            }
    );

    private final ActivityResultLauncher<CropImageContractOptions> cropImageLauncher = registerForActivityResult(
            new CropImageContract(),
            result -> {
                if (result.isSuccessful()) {
                    // Get the cropped image URI
                    Uri croppedImageUri = result.getUriContent();
                    try {
                        // Set the cropped image to the ImageView
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), croppedImageUri);
                        image_added.setImageBitmap(bitmap);
                        // Save the cropped image URI
                        imageUri = croppedImageUri;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (result.getError() != null) {
                    Toast.makeText(this, "Error cropping image: " + result.getError().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private void startCropImage(Uri sourceUri) {
        CropImageContractOptions cropOptions = new CropImageContractOptions.Builder(sourceUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("Crop Image") // Set the title if needed
                .setOutputCompressQuality(90) // Set the compression quality if needed
                .setCropShape(CropImageView.CropShape.RECTANGLE) // Set the crop shape if needed
                .setRequestedSize(1024, 1024) // Set the requested size if needed
                .setOutputUri(Uri.fromFile(new File(getCacheDir(), "cropped"))) // Set the output URI if needed
                .build();
        cropImageLauncher.launch(cropOptions);
    }




    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting");
        progressDialog.show();

        if (imageUri != null) {
            // Your upload logic goes here
        }
    }
}

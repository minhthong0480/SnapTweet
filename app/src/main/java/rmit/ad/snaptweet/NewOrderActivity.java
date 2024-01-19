package rmit.ad.snaptweet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class NewOrderActivity extends AppCompatActivity {
    private ImageView productImage;
    private TextView productName, productDescription, productPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        // Initialize views
        productImage = findViewById(R.id.order_product_image);
        productName = findViewById(R.id.order_product_name);
        productDescription = findViewById(R.id.order_product_description);
        productPrice = findViewById(R.id.order_product_price);

        // Get product details from the intent
        Intent intent = getIntent();
        String productNameText = intent.getStringExtra("pname");
        String productDescriptionText = intent.getStringExtra("description");
        String productPriceText = intent.getStringExtra("price");
        String productImageUrl = intent.getStringExtra("image"); // Make sure to pass the image URL from the previous activity

        // Set product details to the views
        productName.setText("Name: " + productNameText);
        productDescription.setText("Description: " + productDescriptionText);
        productPrice.setText("Price: " + productPriceText);

        // Load image using Picasso
        Picasso.get().load(productImageUrl).into(productImage);
    }
}

package rmit.ad.snaptweet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class NewOrderActivity extends AppCompatActivity {
    private ImageView productImage;
    private TextView productName, productDescription, productPrice;
    private Button orderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        // Initialize views
        productImage = findViewById(R.id.order_product_image);
        productName = findViewById(R.id.order_product_name);
        productDescription = findViewById(R.id.order_product_description);
        productPrice = findViewById(R.id.order_product_price);
        orderButton = findViewById(R.id.order_button);

        // Get product details from the intent
        Intent intent = getIntent();
        final String productNameText = intent.getStringExtra("pname");
        String productDescriptionText = intent.getStringExtra("description");
        final String productPriceText = intent.getStringExtra("price");
        final String productImageUrl = intent.getStringExtra("image"); // Make sure to pass the image URL from the previous activity

        // Set product details to the views
        productName.setText("Name: " + productNameText);
        productDescription.setText("Description: " + productDescriptionText);
        productPrice.setText("Price: " + productPriceText);

        // Load image using Picasso
        Picasso.get().load(productImageUrl).into(productImage);

        // Set click listener for the "Order" button to navigate to PlaceOrderActivity
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPlaceOrderActivity(productNameText, productPriceText, productImageUrl);
            }
        });
    }

    private void navigateToPlaceOrderActivity(String productName, String productPrice, String productImageUrl) {
        Intent intent = new Intent(this, PlaceOrderActivity.class);
        intent.putExtra("pname", productName);
        intent.putExtra("price", productPrice);
        intent.putExtra("image", productImageUrl);
        // Add other necessary data for the new activity
        startActivity(intent);
    }
}

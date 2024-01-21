// PlaceOrderActivity.java
package rmit.ad.snaptweet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class PlaceOrderActivity extends AppCompatActivity {
    private TextView quantityTextView;
    private TextView totalPriceTextView;
    private Button plusButton, minusButton, confirmOrderButton;

    private int quantity = 1; // Initial quantity
    private double unitPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        // Initialize views
        ImageView orderedProductImage = findViewById(R.id.ordered_product_image);
        TextView orderedProductName = findViewById(R.id.ordered_product_name);
        TextView orderedProductPrice = findViewById(R.id.ordered_product_price);
        quantityTextView = findViewById(R.id.quantity_text_view);
        totalPriceTextView = findViewById(R.id.total_price_text_view);
        plusButton = findViewById(R.id.plus_button);
        minusButton = findViewById(R.id.minus_button);
        confirmOrderButton = findViewById(R.id.confirm_order_button);

        // Get product details from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String productName = extras.getString("pname");
            String productPrice = extras.getString("price");
            String productImageUrl = extras.getString("image");

            // Set product details to the views
            orderedProductName.setText("Name: " + productName);
            orderedProductPrice.setText("Price: $" + productPrice);

            // Load image using Picasso
            Picasso.get().load(productImageUrl).into(orderedProductImage);

            // Set initial unit price
            assert productPrice != null;
            unitPrice = Double.parseDouble(productPrice);

            // Set initial values for quantity and total price
            updateQuantityAndTotalPrice();
        }

        // Set click listeners for plus and minus buttons
        plusButton.setOnClickListener(view -> incrementQuantity());

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementQuantity();
            }
        });

        // Set click listener for the Confirm Order button
        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToHomeActivity();
            }
        });
    }

    private void incrementQuantity() {
        quantity++;
        updateQuantityAndTotalPrice();
    }

    private void decrementQuantity() {
        if (quantity > 1) {
            quantity--;
            updateQuantityAndTotalPrice();
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateQuantityAndTotalPrice() {
        quantityTextView.setText("Quantity: " + quantity);
        double totalPrice = unitPrice * quantity;
        totalPriceTextView.setText("Total Price: $" + String.format("%.2f", totalPrice));
    }

    private void navigateToHomeActivity() {
        // Assuming you want to go back to the Home activity
        Intent intent = new Intent(this, ConfirmFinalOrderActivity.class);
        // Add other necessary data for the new activity
        startActivity(intent);
    }
}

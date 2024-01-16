package rmit.ad.snaptweet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Button logoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        logoutBtn.setOnClickListener(view -> {
            Intent intent= new Intent(ProductDetailsActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        Button checkOrdersBtn = (Button) findViewById(R.id.check_orders_btn);


        checkOrdersBtn.setOnClickListener(view -> {
            Intent intent= new Intent(ProductDetailsActivity.this,ProductDetailsActivity.class);
            startActivity(intent);
        });


        ImageView tShirts = (ImageView) findViewById(R.id.t_shirts);
        ImageView sportsTShirts = (ImageView) findViewById(R.id.sports_t_shirts);
        ImageView femaleDresses = (ImageView) findViewById(R.id.female_dresses);
        ImageView sweathers = (ImageView) findViewById(R.id.sweathers);

        ImageView glasses = (ImageView) findViewById(R.id.glasses);
        ImageView hatsCaps = (ImageView) findViewById(R.id.hats_caps);
        ImageView walletsBagsPurses = (ImageView) findViewById(R.id.purses_bags_wallets);
        ImageView shoes = (ImageView) findViewById(R.id.shoes);

        ImageView headPhonesHandFree = (ImageView) findViewById(R.id.headphones_handfree);
        ImageView laptops = (ImageView) findViewById(R.id.laptop_pc);
        ImageView watches = (ImageView) findViewById(R.id.watches);
        ImageView mobilePhones = (ImageView) findViewById(R.id.mobilephones);


        tShirts.setOnClickListener(view -> {
            Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
            intent.putExtra("category", "tShirts");
            startActivity(intent);
        });
        sportsTShirts.setOnClickListener(view -> {
            Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
            intent.putExtra("category", "Sports tShirts");
            startActivity(intent);
        });


        femaleDresses.setOnClickListener(view -> {
            Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
            intent.putExtra("category", "Female Dresses");
            startActivity(intent);
        });


        sweathers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                intent.putExtra("category", "Sweathers");
                startActivity(intent);
            }
        });


        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                intent.putExtra("category", "Glasses");
                startActivity(intent);
            }
        });


        hatsCaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                intent.putExtra("category", "Hats Caps");
                startActivity(intent);
            }
        });



        walletsBagsPurses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                intent.putExtra("category", "Wallets Bags Purses");
                startActivity(intent);
            }
        });


        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                intent.putExtra("category", "Shoes");
                startActivity(intent);
            }
        });



        headPhonesHandFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                intent.putExtra("category", "HeadPhones HandFree");
                startActivity(intent);
            }
        });


        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                intent.putExtra("category", "Laptops");
                startActivity(intent);
            }
        });


        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                intent.putExtra("category", "Watches");
                startActivity(intent);
            }
        });


        mobilePhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ProductDetailsActivity.this,ProductDetailsActivity.class);
                intent.putExtra("category", "Mobile Phones");
                startActivity(intent);
            }
        });
    }
}

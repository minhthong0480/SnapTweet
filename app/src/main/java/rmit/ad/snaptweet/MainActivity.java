package rmit.ad.snaptweet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import rmit.ad.snaptweet.Fragment.HomeFragment;
import rmit.ad.snaptweet.Fragment.MarketFragment;
import rmit.ad.snaptweet.Fragment.ProfileFragment;
import rmit.ad.snaptweet.Fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectFragment = null;
    Button logoutButton;
    ImageButton logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectListener);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String publisher = intent.getString("publisherid");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
            editor.putString("profileid", publisher);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ProfileFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }

        // Set the default fragment when the activity is created
        selectFragment = new HomeFragment();
        loadFragment();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    int itemId = menuItem.getItemId();
                    if (itemId == R.id.nav_home) {
                        selectFragment = new HomeFragment();
                    } else if (itemId == R.id.nav_search) {
                        selectFragment = new SearchFragment();
                    } else if (itemId == R.id.nav_add) {
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                        return true;
                    } else if (itemId == R.id.nav_profile) {
                        selectFragment = new ProfileFragment();
                    } else if (itemId == R.id.nav_market) {
                        selectFragment = new MarketFragment();
                    }

                    // Load the selected fragment
                    loadFragment();
                    return true;
                }
            };

    private void loadFragment() {
        if (selectFragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, selectFragment);
            transaction.commit();
        }
    }
}

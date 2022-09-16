package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener); //we are passing the instance of our listener, down
        viewPager = findViewById(R.id.fragment_container);
        setUpAdapter(viewPager); //ovo je da se runa adapter
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //s ovim listenerom we get swipe na onim fragmentima
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //do not need this
            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //do not need this
            }
        });
    }

    private void setUpAdapter(ViewPager viewPager) //definirali smo adapter da bismo ga mogli koristiti
    {
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPageAdapter.addFragment(new HomeFragment());       // ovo prvi
        viewPageAdapter.addFragment(new MapFragment()); //ovo ce biti drugi indeks
        viewPageAdapter.addFragment(new DashboardFragment()); //treci
        viewPageAdapter.addFragment(new NotificationsFragment()); //cetvrti
        viewPager.setAdapter(viewPageAdapter);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Log.d("DEBUG", "Item clicked!" + item.getItemId());
            switch (item.getItemId()){
                case R.id.nav_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.nav_map:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.nav_dashboard:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.nav_notifications:
                    viewPager.setCurrentItem(3);
                default:
                    return false;
            }
        }
    };
}
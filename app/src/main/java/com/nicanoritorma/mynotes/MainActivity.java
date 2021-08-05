package com.nicanoritorma.mynotes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.nicanoritorma.mynotes.Fragments.FragmentNotes;
import com.nicanoritorma.mynotes.Fragments.PrivacyFragment;
import com.nicanoritorma.mynotes.Fragments.SettingsFragment;
import com.nicanoritorma.mynotes.Fragments.TrashedNotes;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private ActionBar ab;
    private NavigationView navigationView;
    private long onBackPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_tb);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        fab = findViewById(R.id.fab);
        navigationView = findViewById(R.id.nav_view);
        fab.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);

        initUI();
    }

    private void initUI()
    {
        fab.show();
        drawerLayout = findViewById(R.id.drawerLayout);
        ab.setTitle("Notes");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer,
                R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new FragmentNotes()).commit();
        navigationView.setCheckedItem(R.id.menu_notes);
    }

    //open New Note Activity to create new note
    @Override
    public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), NewNote.class));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId())
        {
            case R.id.menu_notes:
                Log.d("FRAGMENT STACK ", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
                initUI();
                break;
            case R.id.menu_delete:
                fab.hide();
                ab.setTitle("Trashed");
                Log.d("FRAGMENT STACK ", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
                transaction.replace(R.id.framelayout, new TrashedNotes()).addToBackStack("trashed").commit();
                navigationView.setCheckedItem(R.id.menu_notes);
                break;
            case R.id.menu_settings:
                fab.hide();
                ab.setTitle("Settings");
                transaction.replace(R.id.framelayout, new SettingsFragment()).commit();
                navigationView.setCheckedItem(R.id.menu_notes);
                break;
            case R.id.menu_privacy:
                fab.hide();
                ab.setTitle("Privacy Policy");
                transaction.replace(R.id.framelayout, new PrivacyFragment()).commit();
                navigationView.setCheckedItem(R.id.menu_notes);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        if (onBackPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            System.exit(0);
            finish();
            return;
        } else {
            Log.d("FRAGMENT STACK ", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
            getSupportFragmentManager().popBackStack();
            initUI();
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        onBackPressedTime = System.currentTimeMillis();
    }
}
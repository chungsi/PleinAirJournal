package com.example.pleinairjournal;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class JournalMenu extends AppCompatActivity{

    ImageButton imageButton_home, imageButton_gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_home);
            toolbar.setNavigationIcon(R.drawable.icon_home);
            getSupportActionBar().setTitle("");

        }

//        imageButton_home = findViewById(R.id.imageButton_home);
//        imageButton_home.setOnClickListener(this);
//
//        imageButton_gallery = findViewById(R.id.imageButton_gallery);
//        imageButton_gallery.setOnClickListener(this);


//        imageButton_home = findViewById(R.id.imageButton_home);
//        imageButton_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(this, "HOME IMAGE BUTTON PRESSED", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext() ,"HOME BUTTON CLICKED", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
//                startActivity(i);
//            }
//        });

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(this, DashboardActivity.class);
//                startActivity(i);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    // JournalMenu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        Log.i("iris", "onCreateOptionsMenuCalled");
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

}

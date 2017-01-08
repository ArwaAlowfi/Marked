package com.example.arwa.marked;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class EditEventActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);

        final Button deleteButton = (Button) findViewById(R.id.delete_btn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //delete selected event:
                // get info about selected event and deleting code thing
                // return to the map
                Intent intent = new Intent(EditEventActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(EditEventActivity.this, "Event Deleted!", Toast.LENGTH_LONG).show();

            }
        });

        final Button addEventbtn = (Button) findViewById(R.id.add_event_btn);
        addEventbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //add event:
                //get text from the text boxes
                //assign to realm object to save in the DB

                Toast.makeText(EditEventActivity.this, "Event added!", Toast.LENGTH_LONG).show();

            }
        });

        final ImageButton takePicBtn = (ImageButton) findViewById(R.id.image_button);
        takePicBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //take pic
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                    //save pic and display on image view up top
                    //Toast.makeText(EditEventActivity.this, "Picture added!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
//    }

        /**
         * A placeholder fragment containing a simple view.
         */

    }
}

package com.example.softwareengineeringproject_2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class DisplayEntryActivity extends AppCompatActivity {

    TextView textview_name_empty, textview_date_empty, textview_composition_empty;
    String item_name, item_position;

    FirebaseDatabase database;
    DatabaseReference myRef;

    String name, date, compositon_final;

    Button displayentry_editbutton, displayentry_deletebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_entry);

        textview_name_empty=findViewById(R.id.textview_name_empty);
        textview_date_empty=findViewById(R.id.textview_date_empty);
        textview_composition_empty=findViewById(R.id.textview_composition_empty);

        displayentry_editbutton=findViewById(R.id.displayentry_editbutton);
        displayentry_deletebutton=findViewById(R.id.displayentry_deletebutton);


        item_name=getIntent().getStringExtra("item_name");
        item_position=getIntent().getStringExtra("item_position");

        final String id_of_item = item_name.substring(0, item_name.indexOf(" "));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("components");

        displayentry_editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DisplayEntryActivity.this, EditEntryActivity.class);
                intent.putExtra("id_from_display", id_of_item);
                startActivity(intent);
            }
        });

        displayentry_deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",item_name);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();


            }
        });

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.


                Map<String, String> data=(Map<String, String>) dataSnapshot.getValue();
                Set keys = data.keySet();
                for (Iterator i = keys.iterator(); i.hasNext(); ) {
                    String key = (String) i.next();
                    String value = (String) data.get(key);
                    Toast.makeText(DisplayEntryActivity.this, key+":"+value, Toast.LENGTH_SHORT).show();
                }
                /*Iterator it = data.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    JSONObject reader = new JSONObject(data);
                    JSONObject jsonObject  = null;
                    try {
                        jsonObject = reader.getJSONObject(String.valueOf(pair.getKey()));
                        name = jsonObject.getString("name");
                        date = jsonObject.getString("date");
                        composition= jsonObject.getString("composition");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    it.remove(); // avoids a ConcurrentModificationException
                }
                textview_name_empty.setText(name);
                textview_date_empty.setText(date);
                textview_composition_empty.setText(compositon_final);





                Map<String, Object> map = (Map<String, Object>) dataSnapshot.child(id_of_item).getValue();
                String value=String.valueOf(map);
                String name=value.substring(value.indexOf('=')+1,value.indexOf(','));
                String date=value.substring(value.indexOf("date")+5, value.indexOf(',', value.indexOf(',') + 1));
                String composition=value.substring(value.indexOf("composition"));
                int l=composition.length();
                String composition_final=composition.substring(12,l-1);*/

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(DisplayEntryActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}

package com.example.softwareengineeringproject_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;



public class EditEntryActivity extends AppCompatActivity {

    EditText editentry_nameedittext, editentry_dateedittext, editentry_compoedittext;

    FirebaseDatabase database;
    DatabaseReference myRef;
    Button editentry_saveentry;

    String id_of_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        editentry_nameedittext=findViewById(R.id.editentry_nameedittext);
        editentry_dateedittext=findViewById(R.id.editentry_dateedittext);
        editentry_compoedittext=findViewById(R.id.editentry_compoedittext);

        id_of_item=getIntent().getStringExtra("id_from_display");


        editentry_saveentry=findViewById(R.id.editentry_saveentry);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("components");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.child(id_of_item).getValue();
                String value=String.valueOf(map);
                String name=value.substring(value.indexOf('=')+1,value.indexOf(','));
                String date=value.substring(value.indexOf("date")+5, value.indexOf(',', value.indexOf(',') + 1));
                String composition=value.substring(value.indexOf("composition"));
                int l=composition.length();
                String composition_final=composition.substring(12,l-1);
                editentry_nameedittext.setText(name);
                editentry_dateedittext.setText(date);
                editentry_compoedittext.setText(composition_final);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(EditEntryActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        editentry_saveentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String name=editentry_nameedittext.getText().toString();
                String date=editentry_dateedittext.getText().toString();
                String composition=editentry_compoedittext.getText().toString();

                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("components");

                myRef.child(id_of_item).child("name").setValue(name);
                myRef.child(id_of_item).child("date").setValue(date);
                myRef.child(id_of_item).child("composition").setValue(composition);


                Intent intent=new Intent(EditEntryActivity.this, ListActivity.class);
                intent.putExtra("component_id_2", id_of_item);
                intent.putExtra("component_name", name);
                intent.putExtra("component_date", date);
                intent.putExtra("component_composition", composition);
                startActivity(intent);

            }
        });





    }
}

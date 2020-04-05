package com.example.softwareengineeringproject_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateEntryActivity extends AppCompatActivity {

    EditText edittext_name, edittext_date, edittext_composition;
    Button save_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_entry);

        edittext_name=findViewById(R.id.edittext_name);
        edittext_date=findViewById(R.id.edittext_date);
        edittext_composition=findViewById(R.id.edittext_composition);

        save_data=findViewById(R.id.save_data);

        save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id=getIntent().getStringExtra("component_id");

                String name=edittext_name.getText().toString();
                String date=edittext_date.getText().toString();
                String composition=edittext_composition.getText().toString();

                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("components");

                myRef.child(id).child("name").setValue(name);
                myRef.child(id).child("date").setValue(date);
                myRef.child(id).child("composition").setValue(composition);


                Intent intent=new Intent(CreateEntryActivity.this, ListActivity.class);
                intent.putExtra("component_id_2", id);
                intent.putExtra("component_name", name);
                intent.putExtra("component_date", date);
                intent.putExtra("component_composition", composition);
                startActivity(intent);





            }
        });

    }
}

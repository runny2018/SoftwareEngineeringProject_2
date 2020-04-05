package com.example.softwareengineeringproject_2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ListActivity extends AppCompatActivity {

    public int LAUNCH_SECOND_ACTIVITY = 1;

    ArrayList<String> listItems;
    ListView listView;
    ArrayAdapter<String> adapter;

    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.listView);
        listItems = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("components");





            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        Map<String, String> data = (Map<String, String>) dataSnapshot.getValue();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            JSONObject reader = new JSONObject(data);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = reader.getJSONObject(String.valueOf(pair.getKey()));
                                String name = jsonObject.getString("name");
                                listItems.add(pair.getKey() + " : " + name);
                                Toast.makeText(ListActivity.this, name, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item_name = listItems.get(position);
                Intent intent = new Intent(ListActivity.this, DisplayEntryActivity.class);
                intent.putExtra("item_name", item_name);
                intent.putExtra("item_position", position);
                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                listItems.remove(result);
                adapter.notifyDataSetChanged();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult


    /*public void addItemToList() {
        id = getIntent().getStringExtra("component_id_2");
        name = getIntent().getStringExtra("component_name");
        date = getIntent().getStringExtra("component_date");
        composition = getIntent().getStringExtra("component_composition");

        if (id != null && name != null) {
            listItems.add(name + " - " + id);
            adapter.notifyDataSetChanged();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.listmenu_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(ListActivity.this, MainActivity.class);
                startActivity(intToMain);
                break;

            case R.id.listmenu_scanner:
                Intent intent = new Intent(ListActivity.this, ScannerActivity.class);
                startActivity(intent);
                break;


        }
        return super.onOptionsItemSelected(item);
    }


}

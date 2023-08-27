package com.example.usercontactapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usercontactapp.adapter.ContactsAdapter;
import com.example.usercontactapp.db.DatabaseHelper;
import com.example.usercontactapp.db.entity.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ContactsAdapter adapter;
    private ArrayList<Contact> contactsArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Favorite Contacts");


        recyclerView = findViewById(R.id.recyclerView);
        db = new DatabaseHelper(this);

        contactsArrayList.addAll(db.getAllContact());

        adapter = new ContactsAdapter(this, contactsArrayList, MainActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAndEditContacts(false, null, -1);
            }
        });

    }

    public void addAndEditContacts(final boolean isUpdated, final Contact contact, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.layout_add_context, null);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setView(view);

        TextView contactTitle = view.findViewById(R.id.new_contact_title);
        final EditText newContact = view.findViewById(R.id.name);
        final EditText contactEmail = view.findViewById(R.id.email);

        contactTitle.setText(!isUpdated ? "Add New Contact":"Edit Contact");

        if(isUpdated && contact!=null){
            newContact.setText(contact.getName());
            contactEmail.setText(contact.getEmail());
        }

        alertDialog.setCancelable(false).setPositiveButton(isUpdated ? "Update" : "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isUpdated){
                    DeleteContact(contact, position);
                }else{
                    dialog.cancel();
                }
            }
        });
        final AlertDialog alertDialogs = alertDialog.create();
        alertDialogs.show();

        alertDialogs.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(newContact.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please Enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    alertDialogs.dismiss();
                }

                if(isUpdated&&contact!=null){
                    UpdateContact(newContact.getText().toString(), contactEmail.getText().toString(),position);
                }else{
                    CreateContact(newContact.getText().toString(), contactEmail.getText().toString());
                }
            }
        });
    }

    private void CreateContact(String toString, String toString1) {
        long id = db.insertContact(toString, toString1);
        Contact contact = db.getContact(id);
        if(contact!=null){
            contactsArrayList.add(0, contact);
            adapter.notifyDataSetChanged();
        }
    }

    private void UpdateContact(String toString, String toString1, int position) {
        Contact contact = contactsArrayList.get(position);

        contact.setName(toString);
        contact.setEmail(toString);

        db.updateContact(contact);

        contactsArrayList.set(position,contact);
        adapter.notifyDataSetChanged();
    }

    private void DeleteContact(Contact contact, int position) {
        contactsArrayList.remove(position);
        db.deleteContact(contact);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.action_setting){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
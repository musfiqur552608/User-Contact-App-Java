package com.example.usercontactapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.usercontactapp.adapter.ContactsAdapter;

public class MainActivity extends AppCompatActivity {

    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
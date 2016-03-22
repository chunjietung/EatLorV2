package com.comp210p.eatlor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button addButton;
    Button sortButton;
    Button hitButton;
    ListView listView;
    ArrayList<String> listItems;
    Context context;
    View view;
    String passResult;
    //ArrayAdapter<String> adapter;
    MyCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        addButton = (Button) findViewById(R.id.addButton);
        listView = (ListView) findViewById(R.id.foodListView);
        listItems = new ArrayList<String>();
        adapter = new MyCustomAdapter(MainActivity.this,R.layout.item,listItems);
        listView.setAdapter(adapter);

        editText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                editText.setCursorVisible(true);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editText.length() == 0)
                {
                    new AlertDialog.Builder(context)
                            .setTitle("No Text")
                            .setMessage("Required to insert food choice")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    editText.setCursorVisible(false);
                }

                else
                {
                    listItems.add(editText.getText().toString());
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                    try{
                        InputMethodManager inputManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                    }
                    catch (Exception e){

                    }
                    editText.setCursorVisible(false);
                }

            }
        });

        context = this;

        sortButton = (Button) findViewById(R.id.sortButton);
        assert sortButton != null;
        sortButton.setOnClickListener(sortClickListener);

        hitButton = (Button) findViewById(R.id.hitButton);
        assert hitButton != null;
        hitButton.setOnClickListener(hitClickListener);
    }

    public String randomFood(ArrayList<String> a){
        int number = (int) (Math.random() * a.size());
        return a.get(number);
    }

    public void onResult(View view) {
        Intent intent = new Intent(this, ReceiveResult.class);
        intent.putExtra("new", passResult);
        startActivity(intent);
    }

    private final View.OnClickListener sortClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            adapter.sort(new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    return lhs.compareTo(rhs);
                }
            });
            adapter.notifyDataSetChanged();
        }
    };

    private final View.OnClickListener hitClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(listItems.size() <= 1) {
                new AlertDialog.Builder(context)
                        .setTitle("Insufficient food choice")
                        .setMessage("Required at least 2 food choices to random")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            else {
                passResult = randomFood(listItems);
                onResult(view);
            }

        }
    };
}
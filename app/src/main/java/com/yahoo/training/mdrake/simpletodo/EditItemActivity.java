package com.yahoo.training.mdrake.simpletodo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class EditItemActivity extends ActionBarActivity {

    int index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String cellContent = getIntent().getStringExtra("cell");
        index = getIntent().getIntExtra("index", 0);
        EditText etTodoCell = ((EditText)findViewById(R.id.etTodoCell));
        etTodoCell.setText(cellContent);
        etTodoCell.setSelection(cellContent.length());


        ((Button)findViewById(R.id.btnSave)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTodoFromText();
            }
        });
        etTodoCell.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            addTodoFromText();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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
    }

    void addTodoFromText(){
        EditText etNewItem = (EditText) findViewById(R.id.etTodoCell);
        String itemText = etNewItem.getText().toString();
        if(itemText.trim().length() == 0){
            return;
        }
        // Prepare data intent
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("cell", itemText);
        data.putExtra("index", index);
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }

}

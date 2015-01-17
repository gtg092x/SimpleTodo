package com.yahoo.training.mdrake.simpletodo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    int REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        ((Button)findViewById(R.id.btnAddItem)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTodoFromText();
            }
        });

        ((EditText)findViewById(R.id.etNewItem)).setOnKeyListener(new View.OnKeyListener() {
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

        setupLiveViewListener();

    }

    void addTodoFromText(){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if(itemText.trim().length() == 0){
            return;
        }
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    // ActivityOne.java, time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String cell = data.getExtras().getString("cell");
            int idx = data.getExtras().getInt("index");
            items.set(idx,cell);
            writeItems();
            itemsAdapter.notifyDataSetChanged();
        }
    }

    private void setupLiveViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("cell", items.get(position));
                i.putExtra("index", position);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    private static final String STATE_FILE = "todo.txt";

    private void readItems(){
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, STATE_FILE);
        try{
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch(IOException e){
            items = new ArrayList<String>();
        }
    }

    private void writeItems(){
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, STATE_FILE);
        try{
            FileUtils.writeLines(todoFile,items);
        } catch(IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}

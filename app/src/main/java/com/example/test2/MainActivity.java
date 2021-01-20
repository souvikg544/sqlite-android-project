package com.example.test2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private GroceryAdapter mAdapter;
    private SQLiteDatabase mDatabase;
    private EditText mEditTextName;
    private EditText mEditTextUnit;
    private TextView mTextViewAmount;
    private EditText msearch;
    private int mAmount = 0;
    private String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GrocceryDBHelper dbHelper=new GrocceryDBHelper(this);
        mDatabase=dbHelper.getWritableDatabase();

        RecyclerView recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new GroceryAdapter(this,getAllItems());
        recyclerView.setAdapter(mAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){


            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());


            }
        }).attachToRecyclerView(recyclerView);
        mEditTextName = findViewById(R.id.edittext_name);
        mTextViewAmount = findViewById(R.id.textview_amount);
        mEditTextUnit=findViewById(R.id.edittext_unit);

        Button buttonIncrease = findViewById(R.id.button_increase);
        Button buttonDecrease = findViewById(R.id.button_decrease);
        Button buttonAdd = findViewById(R.id.button_add);
        buttonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increase();
            }
        });
        buttonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrease();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
                closeKeyboard();
            }
        });

    }
    private void increase()
    {
        mAmount++;
        mTextViewAmount.setText(String.valueOf(mAmount));

    }
    private void decrease(){
        if(mAmount>0)
            mAmount--;
        mTextViewAmount.setText(String.valueOf(mAmount));

    }
    private void add(){
        if(mEditTextName.getText().toString().trim().length()==0 || mAmount==0)
        {
            return;
        }
        String name=mEditTextName.getText().toString();
        String unit=mEditTextUnit.getText().toString();
        ContentValues cv=new ContentValues();
        cv.put(GroceryContract.GroceryEntry.COLUMN_NAME,name);
        cv.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT,mAmount);
        cv.put(GroceryContract.GroceryEntry.COLUMN_UNIT,unit);
        mDatabase.insert(GroceryContract.GroceryEntry.TABLE_NAME,null,cv);
        mAdapter.swapCursor(getAllItems());
        mEditTextName.getText().clear();
        msearch.getText().clear();
       // mEditTextUnit.getText().clear();


    }
    private void removeItem(Long id)
    {
        mDatabase.delete(GroceryContract.GroceryEntry.TABLE_NAME,
                GroceryContract.GroceryEntry._ID+"="+id,null);
    }

    private Cursor getAllItems()
    {
        search=msearch.getText().toString();
        if(search.trim().length()!=0){
            

        }
        return mDatabase.query(
                GroceryContract.GroceryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                GroceryContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
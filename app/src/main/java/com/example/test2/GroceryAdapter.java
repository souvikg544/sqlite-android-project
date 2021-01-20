package com.example.test2;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder> {
    private Context mcontext;
    private Cursor mcursor;
    public GroceryAdapter(Context context, Cursor cursor){
        mcontext=context;
        mcursor=cursor;
    }
    public class GroceryViewHolder extends RecyclerView.ViewHolder{
        public TextView nametext;
        public TextView counttext;
        public TextView unittext;

        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);
            nametext=itemView.findViewById(R.id.textview_name_item);
            counttext=itemView.findViewById(R.id.textview_amount_item);
            unittext=itemView.findViewById(R.id.textview_unit_name);
        }
    }

    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(R.layout.grocery_item, parent, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder holder, int position) {
        if (!mcursor.moveToPosition(position)) {
            return;
        }
        String name = mcursor.getString(mcursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_NAME));
        String unit = mcursor.getString(mcursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_UNIT));
        int amount = mcursor.getInt(mcursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_AMOUNT));
        Long id=mcursor.getLong(mcursor.getColumnIndex(GroceryContract.GroceryEntry._ID));
        holder.nametext.setText(name);
        holder.counttext.setText(String.valueOf(amount));
        holder.unittext.setText(unit);
        holder.itemView.setTag(id);

    }

    @Override
    public int getItemCount() {
        return mcursor.getCount();
    }
    public void swapCursor(Cursor newCursor) {
        if (mcursor != null) {
            mcursor.close();
        }
        mcursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}

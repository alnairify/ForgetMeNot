package com.example.forgetmenot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.forgetmenot.db.DatabaseAccess;

import java.util.List;

public class MainActivity extends ActionBarActivity {
    private GridView gridView;
    private Button btnAdd;
    private DatabaseAccess databaseAccess;
    private List<Memo> memos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.databaseAccess = DatabaseAccess.getInstance(this);

        this.gridView = (GridView) findViewById(R.id.GridView);
        this.btnAdd = (Button) findViewById(R.id.btnAdd);

        this.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClicked();
            }
        });

        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memo memo = memos.get(position);
                /*TextView txtMemo = (TextView) view.findViewById(txtMemo);
                if (memo.isFullDisplayed()) {
                    txtMemo.setText(memo.getShortText());
                    memo.setFullDisplayed(false);
                } else {
                    txtMemo.setText(memo.getText());
                    memo.setFullDisplayed(true);
                }*/
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseAccess.open();
        this.memos = databaseAccess.getAllMemos();
        databaseAccess.close();
        MemoAdapter adapter = new MemoAdapter(this, memos);
        this.gridView.setAdapter(adapter);
    }

    public void onAddClicked() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

    public void onDeleteClicked(Memo memo) {
        databaseAccess.open();
        databaseAccess.delete(memo);
        databaseAccess.close();

        ArrayAdapter<Memo> adapter = (ArrayAdapter<Memo>) gridView.getAdapter();
        adapter.remove(memo);
        adapter.notifyDataSetChanged();
    }

    public void onEditClicked(Memo memo) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("MEMO", memo);
        startActivity(intent);
    }

    private class MemoAdapter extends ArrayAdapter<Memo> {


        public MemoAdapter(Context context, List<Memo> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_grid_item, parent, false);
            }

            ImageView btnEdit = (ImageView) convertView.findViewById(R.id.imageView);
            //ImageView btnDelete = (ImageView) convertView.findViewById(R.id.btnDelete);
            //TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            //TextView txtMemo = (TextView) convertView.findViewById(R.id.txtMemo);

            final Memo memo = memos.get(position);
            memo.setFullDisplayed(false);
            //txtDate.setText(memo.getDate());
            //txtMemo.setText(memo.getShortText());
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditClicked(memo);
                }
            });
            /*btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClicked(memo);
                }
            });*/
            return convertView;
        }
    }
}
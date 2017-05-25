package com.zy.animaldemo1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> list;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("name:" + i);
        }
        mListView = ((ListView) findViewById(R.id.lv));
        mListView.setAdapter(new MyAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("name", list.get(position));
                EasyTransitionOptions options = EasyTransitionOptions.makeTransitionOptions(MainActivity.this,
                        view.findViewById(R.id.iv_icon),
                        view.findViewById(R.id.tv_name),
                        findViewById(R.id.v_top_card));
                EasyTransition.startActivity(intent, options);

            }
        });

    }

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            int count = 0;
            if (null != list) {
                count = list.size();
            }
            return count;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (null != convertView) {
                view = convertView;
            } else {
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_list, null);
            }
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            tvName.setText(list.get(position));
            return view;
        }
    }
}

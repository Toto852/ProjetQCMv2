package org.libreapps.projetqcm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private Button buttonMode1;
    private Button buttonMode2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Param.getInstance().setListQizz(getListData());
        buttonMode1 =  (Button)findViewById(R.id.Mode1);
        buttonMode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                startActivity(intent);
            }
            });

            buttonMode2 =  (Button)findViewById(R.id.buttonMode2);

            buttonMode2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SurvieActivity.class);
                    startActivity(intent);
                }

        });

        /*
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new CustomListAdapter(this, listData));
        // When the user clicks on the ListItem
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                Product upload = (Product) o;
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("id", upload.getId());
                intent.putExtra("name", upload.getName());
                intent.putExtra("type", upload.getType());
                intent.putExtra("price", upload.getPrice());
                startActivity(intent);
            }
        });
         */

    }

    public ArrayList<Quizz> getListData(){
        try{
            ConnectionRest connectionRest = new ConnectionRest();
            // connectionRest.setAction("quizz");
            connectionRest.execute("GET");
            String listJsonObjs = connectionRest.get();
            if(listJsonObjs != null) {
                return parseQuizz(listJsonObjs);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Quizz> parseQuizz(final String json) {
        try {
            Log.v("ListQuizz", json);//TODO
            final ArrayList<Quizz> quizzes = new ArrayList<>();
            final JSONArray jProductArray = new JSONArray(json);
            for (int i = 0; i < jProductArray.length(); i++) {
                quizzes.add(new Quizz(jProductArray.optJSONObject(i)));
                //Log.v("Quizz", quizzes.toString());//TODO
            }
            return quizzes;
        } catch (JSONException e) {
            Log.v("TAG","[JSONException] e : " + e.getMessage());
        }
        return null;
    }


}

package ks.hs.emirim.hyojin.project12_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    MyDBHelper dbHelper;
    EditText editName, editCount, editResult, editNameResult, editCountResult;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.edit_name);
        editCount = findViewById(R.id.edit_count);
        editResult = findViewById(R.id.edit_count);
        editNameResult = findViewById(R.id.edit_name_result);
        editCountResult = findViewById(R.id.edit_count_result);
        Button btnInit = findViewById(R.id.btn_init);
        Button btnInput = findViewById(R.id.btn_input);
        Button btnsearch = findViewById(R.id.btn_search);

        dbHelper = new MyDBHelper(this);
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = dbHelper.getWritableDatabase();
                dbHelper.onUpgrade(db, 1,2);
                db.close();
            }
        });
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = dbHelper.getWritableDatabase();
                db.execSQL("insert into groupTB values('"+editName.getText().toString()+"',"+editCount.getText().toString()+")");
                db.close();
                Toast.makeText(getApplicationContext(),"정상적으로 행이 삽입되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = dbHelper.getReadableDatabase();
                Cursor cusor = db.rawQuery("select * from groupTB;", null);
                String strName = "그룹 이름\r\n______\r\n";
                String strCount = "인원\r\n______\r\n";
                while (cusor.moveToNext()){
                    strName += cusor.getString(0) + "\r\n";
                    strCount += cusor.getInt(1) + "\r\n";
                }

                editNameResult.setText(strName);
                editCountResult.setText(strCount);

                cusor.close();
                db.close();
            }
        });
    }
    public class MyDBHelper extends SQLiteOpenHelper{

        public MyDBHelper(Context context){
            super(context, "groupDB", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table groupTB(name char(20) primary key, count integer);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists groupTB ");
            onCreate(db);
        }
    }
}
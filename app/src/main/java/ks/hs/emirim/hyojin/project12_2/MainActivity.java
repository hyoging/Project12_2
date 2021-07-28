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
    EditText editName, editCount, editNameResult, editCountResult;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName = findViewById(R.id.edit_name);
        editCount = findViewById(R.id.edit_count);
        editNameResult = findViewById(R.id.edit_name_result);
        editCountResult = findViewById(R.id.edit_count_result);
        Button btnInit = findViewById(R.id.btn_init);
        Button btnInput = findViewById(R.id.btn_input);
        Button btnUpdate = findViewById(R.id.btn_update);
        Button btnDelete = findViewById(R.id.btn_delete);
        Button btnSearch = findViewById(R.id.btn_search);

        dbHelper = new MyDBHelper(this);



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
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = dbHelper.getWritableDatabase();
                db.execSQL("update groupTB set count = " + editCount.getText().toString() + " where name = '" + editName.getText().toString() + "';");
                selectDB();
                db.close();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = dbHelper.getWritableDatabase();
                db.execSQL("delete from groupTB where name = '" + editName.getText().toString() + "';");
                selectDB();
                db.close();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
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

    public void selectDB(){
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from groupTB;", null);
        String strName = "그룹 이름\r\n_________\r\n";
        String strCount = "인원\r\n_________\r\n";
        while(cursor.moveToNext()){
            strName += cursor.getString(0) + "\r\n";
            strCount += cursor.getInt(1) + "\r\n";
        }
        editNameResult.setText(strName);
        editCountResult.setText(strCount);

        cursor.close();
        db.close();
    }
    public class MyDBHelper extends SQLiteOpenHelper{

        public MyDBHelper(Context context){
            super(context, "groupDB", null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE groupTB (name CHAR(20) PRIMARY KEY, count INTEGER);");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists groupTB");
            onCreate(db);
        }
    }
}
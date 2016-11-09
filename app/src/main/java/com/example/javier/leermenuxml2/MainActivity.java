package com.example.javier.leermenuxml2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView texto;
    TextView primer;
    TextView segundo;
    TextView postre;
    DatePicker fecha;
    SQLiteDatabase db;
    GridView tabla;
    Cursor c;
    UsuariosSQLiteHelper usuHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuHelp = new UsuariosSQLiteHelper(this, "DBMenus", null, 1);


        new MiTarea().execute("http://informaticasalesianoszgz.org.es/menus.xml");//Ejecuta el metodo execute para llamar a doInBackground pasandole la url
        texto = (TextView) findViewById(R.id.Texto);
        primer = (TextView) findViewById(R.id.Primer);
        segundo = (TextView) findViewById(R.id.Segundo);
        postre = (TextView) findViewById(R.id.Postre);
        Button boton = (Button) findViewById(R.id.Mostrar);
        fecha = (DatePicker) findViewById(R.id.date);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Onclick
            String dia;
            String mes;

            if(fecha.getDayOfMonth()<10) {
                 dia = "0"+fecha.getDayOfMonth()+"/";
            }else{
                dia = fecha.getDayOfMonth()+"/";
            }
            if((fecha.getMonth() + 1)<10) {
                mes = "0"+(fecha.getMonth()+1)+"/";
            }else{
                mes = (fecha.getMonth()+1)+"/";
            }
            int año = fecha.getYear();

            String fechafinal = dia+mes+año;
            MenuDia(fechafinal);
            }
        });
    }

    public void MenuDia(String f){
        db = usuHelp.getReadableDatabase();
        //Es un conjunto de registros el resulset
        String[] args = new String[] {f};
        Cursor cursor = db.rawQuery("SELECT * FROM Menus WHERE fecha=?", args);
        if(cursor.moveToFirst()){
            do {
                primer.setText(cursor.getString(0));
                segundo.setText(cursor.getString(1));
                postre.setText(cursor.getString(2));
                texto.setText(cursor.getString(3));
            }while(cursor.moveToNext());
        }

    }

    public class MiTarea extends AsyncTask<String,Float,Integer> {
        List<Menu> menu;

        @Override
        protected Integer doInBackground(String... params) {
            RssParserSax saxparser =new RssParserSax(params[0]);
            menu = saxparser.cargar();
            publishProgress(250f);

            return null;
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);
            db = usuHelp.getWritableDatabase();
            db.delete("Menus","",null);//Para Actualizar borra para que no se repitean
            if(db != null) {
                for (int i = 0; i < menu.size(); i++) {

                    String primerPlato1 = menu.get(i).getP1();
                    String segundoPlato1 = menu.get(i).getP2();
                    String postre1 = menu.get(i).getPostre();
                    String fecha1 = menu.get(i).getFecha();

                    ContentValues nuevoRegistro = new ContentValues();
                    nuevoRegistro.put("primerPlato", primerPlato1);
                    nuevoRegistro.put("segundoPlato", segundoPlato1);
                    nuevoRegistro.put("postre", postre1);
                    nuevoRegistro.put("fecha", fecha1);
                    db.insert("Menus", null, nuevoRegistro);
                }
                //Cerramos la base de datos
                db.close();
            }
        }
    }
}


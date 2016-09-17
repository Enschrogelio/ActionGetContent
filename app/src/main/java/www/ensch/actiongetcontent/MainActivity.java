package www.ensch.actiongetcontent;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer mediaPlayer;
    TextView tv;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=(TextView) findViewById(R.id.textView);
        Button boton=(Button) findViewById(R.id.button1);
        boton.setOnClickListener(this);
        imageView=(ImageView) findViewById(R.id.imageView1);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // intent para personalizar el diálogo usando  ACTION_CHOOSER
        Intent intent2=Intent.createChooser(intent,"Usando ACTION_CHOOSER");
        startActivityForResult(intent2,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){

        try{
            Uri uri=intent.getData();
            tv.setText("\nURI="+uri.toString());
            String mime=getContentResolver().getType(uri);
            tv.append("\nMIME="+mime);

            if(mime.matches("image.*")){
                tv.append("\nmostrando imagen");
                imageView.setImageURI(uri);
            }
            else if(mime.matches("audio.*")){
                if(mediaPlayer!=null)mediaPlayer.release();
                mediaPlayer=MediaPlayer.create(this, uri);
                mediaPlayer.start();

                String[] columnas={MediaStore.Audio.Media.DISPLAY_NAME};
                Cursor cursor=getContentResolver().query(uri, columnas, null,null,null);
                cursor.moveToFirst();
                String name=cursor.getString(0);
                tv.append("\nreproduciendo fichero de audio");
                tv.append("\n"+name);
            }

        } catch(Exception e){
            tv.setText("\nNingún fichero seleccionado");
        }

    }

    @Override
    public void onPause(){
        super.onPause();
        if(mediaPlayer!=null)mediaPlayer.release();
    }

}
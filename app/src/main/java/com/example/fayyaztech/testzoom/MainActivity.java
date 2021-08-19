package com.example.fayyaztech.testzoom;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button enableZoomBtn;
    private DrawableView drawbleView;
    private CustomImageView touchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawbleView = findViewById(R.id.drawble_view);
        enableZoomBtn = findViewById(R.id.enable_zoom);
        touchImageView = findViewById(R.id.zoom_iv);
        enableZoomBtn.setOnClickListener(this);
        drawbleView.setDrawingEnabled(false);

        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {


                File folder = new File(Environment.getExternalStorageDirectory().toString());
                boolean success = false;
                if (!folder.exists())
                {
                    success = folder.mkdirs();
                }

                System.out.println(success+"folder");

                File file = new File(Environment.getExternalStorageDirectory().toString() + "/sample.png");

                if ( !file.exists() )
                {
                    try {
                        success = file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println(success+"file");



                FileOutputStream ostream = null;
                try
                {
                    ostream = new FileOutputStream(file);

                    System.out.println(ostream);
                    View targetView = drawbleView;

                    // myDrawView.setDrawingCacheEnabled(true);
                    //   Bitmap save = Bitmap.createBitmap(myDrawView.getDrawingCache());
                    //   myDrawView.setDrawingCacheEnabled(false);
                    // copy this bitmap otherwise distroying the cache will destroy
                    // the bitmap for the referencing drawable and you'll not
                    // get the captured view
                    //   Bitmap save = b1.copy(Bitmap.Config.ARGB_8888, false);
                    //BitmapDrawable d = new BitmapDrawable(b);
                    //canvasView.setBackgroundDrawable(d);
                    //   myDrawView.destroyDrawingCache();
                    // Bitmap save = myDrawView.getBitmapFromMemCache("0");
                    // myDrawView.setDrawingCacheEnabled(true);
                    //Bitmap save = myDrawView.getDrawingCache(false);
                    Bitmap well = drawbleView.getBitmap();
                    Bitmap save = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
                    Paint paint = new Paint();
                    paint.setColor(Color.WHITE);
                    Canvas now = new Canvas(save);
                    now.drawRect(new Rect(0,0,320,480), paint);
                    now.drawBitmap(well, new Rect(0,0,well.getWidth(),well.getHeight()), new Rect(0,0,320,480), null);

                    // Canvas now = new Canvas(save);
                    //myDrawView.layout(0, 0, 100, 100);
                    //myDrawView.draw(now);
                    if(save == null) {
                        System.out.println("NULL bitmap save\n");
                    }
                    save.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    //bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    //ostream.flush();
                    //ostream.close();
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Null error", Toast.LENGTH_SHORT).show();
                }

                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "File error", Toast.LENGTH_SHORT).show();
                }

                catch (IOException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "IO error", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.enable_zoom:
                if(enableZoomBtn.getText().equals("disable zoom")){
                    touchImageView.setZoomEnable(false);
                    drawbleView.setDrawingEnabled(true);
                    enableZoomBtn.setText("enable zoom");
                } else{
                    touchImageView.setZoomEnable(true);
                    drawbleView.setDrawingEnabled(false);
                    enableZoomBtn.setText("disable zoom");
                }
                break;

            default:
                break;
        }
    }
    fun getDownloadsPath(fileName: String, mimeType: String?): String {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {

            var path = ""
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, mimeType?.let { MimeTypes.lookupExtType(it) })
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.Downloads.IS_PENDING, 1)
            }

            val collection = MediaStore.Downloads.EXTERNAL_CONTENT_URI
            val imageUri = Utils.application.contentResolver.insert(collection, values)

            path  = getRealPathFromUri(imageUri)?.substringBeforeLast("/").plus(fileName)
            return Utils.application.filesDir.path + "/" + fileName

        } else {
            val file = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS).absolutePath + fileName

            return file
        }
    }
}

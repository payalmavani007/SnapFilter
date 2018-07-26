package com.android.facefilter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.sql.Types.NULL;

public class Preview extends AppCompatActivity {
    Bitmap bitmap;
    File folder;
    String timeStamp;
    File file;
    String root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_activity);
        final ImageView imageView = (ImageView) findViewById(R.id.preview_image);
        ImageView text = (ImageView) findViewById(R.id.text);
        ImageView crop = (ImageView) findViewById(R.id.crop);
        ImageView cancle = (ImageView) findViewById(R.id.cancle);
        ImageView delete = (ImageView) findViewById(R.id.delete);
        ImageView share = (ImageView) findViewById(R.id.share_preview);
        ImageView stiker = (ImageView) findViewById(R.id.stiker);
        ImageView preview_download = (ImageView) findViewById(R.id.preview_download);
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        final Intent intent = getIntent();
        final String image = intent.getStringExtra("image");

        Picasso.with(getApplicationContext())
                .load(image)
                .into(imageView);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = getBitmap(relativeLayout); // Load your bitmap here
                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(10);
                canvas.drawText("Some Text here", 50, 50, paint);
        }
        });
        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropAndGivePointedShape(bitmap);
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Preview.this,FaceFilterActivity.class);
                startActivity(intent1);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        stiker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        preview_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = getBitmap(relativeLayout);
                saveChart(bitmap, relativeLayout.getMeasuredHeight(), relativeLayout.getMeasuredWidth(), image);
                Toast.makeText(getApplicationContext(), " Download Successfull.", Toast.LENGTH_SHORT).show();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap1 = getBitmap(relativeLayout);
                saveChart(bitmap1, relativeLayout.getMeasuredHeight(), relativeLayout.getMeasuredWidth(),image);
                String fileName = image + ".png";
                String externalStorageDirectory = Environment.getExternalStorageDirectory().toString();
                String myDir = externalStorageDirectory + "/TestXyz/"; // the file will be in saved_images
                Uri uri = Uri.parse("file:///" + myDir + fileName);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Testing");
                shareIntent.putExtra(Intent.EXTRA_TEXT, " Images");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(shareIntent, "Share Deal"));
            }
        });

    }

    public Bitmap getBitmap(RelativeLayout layout) {
        layout.setDrawingCacheEnabled(true);
        layout.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(layout.getDrawingCache());
        layout.setDrawingCacheEnabled(false);
        return bmp;
    }

    private void saveChart(Bitmap getbitmap, float height, float width, String name) {

        root = Environment.getExternalStorageDirectory().toString();
        folder = new File(root + "/PreviewImage");
        boolean success = false;

        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss",
                Locale.getDefault()).format(new Date());
        file = new File(folder.getPath() + File.separator + name + ".png");

        if (!file.exists()) {
            try {
                success = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream ostream = null;

        try {
            ostream = new FileOutputStream(file);
            System.out.println(ostream);
            Bitmap well = getbitmap;
            Bitmap save = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            Canvas now = new Canvas(save);
            now.drawRect(new Rect(0, 0, (int) width, (int) height), paint);
            now.drawBitmap(well,
                    new Rect(0, 0, well.getWidth(), well.getHeight()),
                    new Rect(0, 0, (int) width, (int) height), null);

            if (save == null) {
                System.out.println(NULL);
            }
            save.compress(Bitmap.CompressFormat.PNG, 100, ostream);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Bitmap cropAndGivePointedShape(Bitmap originalBitmap)
    {
        Bitmap bmOverlay = Bitmap.createBitmap(originalBitmap.getWidth(),
                originalBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(originalBitmap, 0, 0, null);
        canvas.drawRect(0, 0, 20, 20, p);

        Point a = new Point(0, 20);
        Point b = new Point(20, 20);
        Point c = new Point(0, 40);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();

        canvas.drawPath(path, p);
        a = new Point(0, 40);
        b = new Point(0, 60);
        c = new Point(20, 60);

        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();

        canvas.drawPath(path, p);

        canvas.drawRect(0, 60, 20, originalBitmap.getHeight(), p);

        return bmOverlay;
    }
}

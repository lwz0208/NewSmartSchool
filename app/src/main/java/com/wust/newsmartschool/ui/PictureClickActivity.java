package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.wust.newsmartschool.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class PictureClickActivity extends Activity {

    ImageView pc_imageView;
    int id_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_picture_click);

        pc_imageView = (ImageView) findViewById(R.id.pc_imageView);
        id_picture = getIntent().getExtras().getInt("id_picture");
        pc_imageView.setImageResource(id_picture);

        pc_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pc_imageView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        PictureClickActivity.this);
                builder.setTitle("提示：");
                builder.setMessage("是否保存到本地？");
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                InputStream is = getResources().openRawResource(id_picture);
                                Bitmap bitmap = BitmapFactory.decodeStream(is);
                                File file = new File("/sdcard/wust_image/");
                                if (!file.exists()) {
                                    file.mkdir();
                                }
                                File imageFile = new File(file, id_picture
                                        + ".jpg");
                                try {
                                    imageFile.createNewFile();
                                    FileOutputStream fos=new FileOutputStream(imageFile);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                                    fos.flush();
                                    fos.close();
                                    Toast.makeText(PictureClickActivity.this,
                                            "已保存到/sdcard/wust_image/", Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    Toast.makeText(PictureClickActivity.this,
                                            "保存失败！", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }

                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });
    }
}


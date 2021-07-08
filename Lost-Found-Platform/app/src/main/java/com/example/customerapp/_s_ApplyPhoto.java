package com.example.customerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class _s_ApplyPhoto extends AppCompatActivity implements IP {

    public String getIP() {
        return IP_address;
    }

    private String IP_ADDRESS = getIP();


    //이미지 관련 변수
    ImageView iv;
    //업로드할 이미지의 절대경로(실제 경로) - 갤러리
    String imgPath;
    //카메라
    String mCurrentPhotoPath;
    //카메라 인지 갤러리인지 확인
    int flag=5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._s_apply_photo);
        iv=findViewById(R.id.iv);


        //권한 추가
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Log.d(TAG, "권한 설정 완료");
            } else {
                //Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(_s_ApplyPhoto.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            }
        }

    }//onCreate() ..


    //갤러리 실행
    public void clickBtn(View view) {

        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,10);
    }

    //카메라 실행
    public void clickBtn2(View view) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 11);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //갤러리 수행 시
            case 10:
                if (resultCode == RESULT_OK) {
                    //선택한 사진의 경로(Uri)객체 얻어오기
                    Uri uri = data.getData();
                    if (uri != null) {
                        flag=10;
                        iv.setImageURI(uri);

                        imgPath = getRealPathFromUri(uri);   //임의로 만든 메소드 (절대경로를 가져오는 메소드)

                        //이미지 경로 uri 확인해보기
                        new AlertDialog.Builder(this, R.style._s_MyDialogTheme).setMessage(uri.toString() + "\n" + imgPath).create().show();
                        //Upload();
                    }

                } else {
                    Toast.makeText(this, "이미지 선택을 하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                break;

            //카메라 수행 시
            case 11:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, mCurrentPhotoPath, Toast.LENGTH_SHORT).show();
                    File file = new File(mCurrentPhotoPath);
                    Bitmap bitmap;
                    if (Build.VERSION.SDK_INT >= 29) {

                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));
                        //Toast.makeText(this, "카메라 성공!!!!!!", Toast.LENGTH_SHORT).show();
                        try {
                            bitmap = ImageDecoder.decodeBitmap(source);
                            if (bitmap != null) {
                                flag=11;
                                iv.setImageBitmap(bitmap);
                                // Upload();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                            if (bitmap != null) {
                                flag=11;
                                iv.setImageBitmap(bitmap);
                                //  Upload();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            ;
                        }
                    }
                    break;

                } else {
                    Toast.makeText(this, "카메라 실패!!!!!!!", Toast.LENGTH_SHORT).show();
                }
                break;
            ///////////////////////////////////////////////////////////
        }
    }//onActivityResult() ..


    //사진 촬영 후 이미지 저장
    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    //Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    String getRealPathFromUri(Uri uri){
        String[] proj= {MediaStore.Images.Media.DATA};
        CursorLoader loader= new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor= loader.loadInBackground();
        int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result= cursor.getString(column_index);
        cursor.close();
        return  result;
    }

    //DB에 이미지 업로드
    public void clickUpload(View view) {

        //안드로이드에서 보낼 데이터를 받을 php 서버 주소
        String serverUrl="http://"+IP_ADDRESS+"/insertDB.php";

        //Volley plus Library를 이용해서 파일 전송
        //파일 전송 요청 객체 생성[결과를 String으로 받음]
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                new AlertDialog.Builder(_s_ApplyPhoto.this, R.style._s_MyDialogTheme).setMessage("응답:"+response).create().show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(_s_ApplyPhoto.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        //이미지 파일 추가
        if (flag==10) {
            //이미지 파일 추가
            smpr.addFile("img", imgPath);

        } else if (flag==11){
            //사진 찍고 바로 추가
            smpr.addFile("img", mCurrentPhotoPath);

        }

        //요청객체를 서버로 보낼 우체통 같은 객체 생성
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(smpr);

    }

}
package com.example.android.qrthree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

public class MainActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private Button btnScan;
    String[] PERMISSIONS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        //TextView textView = findViewById(R.id.textView1);
        btnScan = findViewById(R.id.btnScan);
        PERMISSIONS = new String[]{
                Manifest.permission.CAMERA
        };
        if(!hasPermisstions(getApplicationContext(),PERMISSIONS)){
            ActivityCompat.requestPermissions(MainActivity.this,PERMISSIONS,1);
        }
        mCodeScanner = new CodeScanner(this, scannerView);

        mCodeScanner.setFlashEnabled(true);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
                mCodeScanner.setDecodeCallback(new DecodeCallback() {
                    @Override
                    public void onDecoded(@NonNull final Result result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                                String data = result.getText();
                                //textView.setText(data);

                                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                                intent.putExtra("data",data);
                                startActivity(intent);

                            }
                        });
                    }
                });
            }
        });
    }
    private boolean hasPermisstions(Context context,String... PERMISSIONS){
        if(context!=null && PERMISSIONS!= null){
            for(String permission: PERMISSIONS){
                if(ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
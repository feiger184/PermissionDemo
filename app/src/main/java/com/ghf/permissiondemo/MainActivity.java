package com.ghf.permissiondemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.Manifest.permission.CALL_PHONE;

public class MainActivity extends AppCompatActivity {

    private static final int CALL_PHONE_REQUEST = 1;
    private Button btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCall = (Button) findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testCall();
            }
        });
    }

    //检测权限是否授权
    private void testCall() {
        //1.检测权限
        int i = ContextCompat.checkSelfPermission(this, CALL_PHONE);

        if (i != PackageManager.PERMISSION_GRANTED) {

            //清单里面授权被拒绝 向用户申请

            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{CALL_PHONE}, CALL_PHONE_REQUEST);

        } else {
            callPhone();
        }


    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:" + "10010");
        intent.setData(uri);
        startActivity(intent);

    }

    //权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CALL_PHONE_REQUEST) {
            //判断权限是否授权了

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户真正同意了授权
                callPhone();
            } else {
                //用户给拒绝
                Toast.makeText(this, "权限拒绝了", Toast.LENGTH_SHORT).show();

                //用户彻底拒绝权限后
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {

                    showDialog();
                }

            }
        }
    }

    public void showDialog() {
        new AlertDialog.Builder(this)
                .setMessage("权限被彻底拒绝，请到设置里面打开，才能使用此功能")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //跳转到本应用的设置页面 开权限
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }
}

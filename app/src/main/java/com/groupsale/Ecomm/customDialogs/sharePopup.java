package com.groupsale.Ecomm.customDialogs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.groupsale.Ecomm.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class sharePopup extends Dialog implements View.OnClickListener {

    private static final String LOG_TAG = "Record_Log";
    ImageButton record;

    LottieAnimationView mic;
    String AudioFilePath;//filepath for audio
    StorageReference srAudio;
    DatabaseReference drAudio;

    private Chronometer AudioTimer;
    private TextView RecordStatus;


    Boolean recording = false;
    Context mcontext;

    MediaRecorder mediaRecorder;

    String deal;
    TextView sharePopupText, timer;
    Button share;
    int flag =0;

    Uri audiouri;
    ParcelFileDescriptor file;


    public sharePopup(@NonNull Context context, String dealId) {
        super(context);

        deal = dealId;
        mcontext = context;

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.share_popup);


        drAudio = FirebaseDatabase.getInstance().getReference().child("dealsDB").child(deal);

        mic = findViewById(R.id.Recording);

        AudioTimer = findViewById(R.id.record_timer);
        RecordStatus = findViewById(R.id.Audio_Status);

        AudioFilePath = Environment.getStorageDirectory().getAbsolutePath();
        AudioFilePath += "/DealAudio.mp3";

        Log.d("LOG_TAG", "path" + AudioFilePath);

        findViewById(R.id.voice_recorder).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (recording) {
                    if (flag == 1) {
                        stopAudio();
                        recording=false;
                        mic.clearAnimation();
                        mic.cancelAnimation();
                        mic.setFrame(0);
                        RecordStatus.setVisibility(View.VISIBLE);
                        RecordStatus.setText("TAP TO RECORD");
                        AudioTimer.setVisibility(View.INVISIBLE);
                    } else {
                        Toast.makeText(getContext(), "Permissions not granted", LENGTH_SHORT).show();
                    }
                }
                else{

                        checkPermissionsAndStart();
                    }
                }

        });

        sharePopupText = findViewById(R.id.txt_share);
        timer = findViewById(R.id.share_popup_timer);
        share = findViewById(R.id.share_popup_share);
        new CountDownTimer(86400000, 1000) {
            public void onTick(long millisUntilFinished) {

                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                String time = f.format(hour) + ":" + f.format(min) + ":" + f.format(sec);
                timer.setText(time);
            }

            public void onFinish() {
                timer.setText("00:00:00");
            }
        }.start();


        sharePopupText.setText(getContext().getResources().getString(R.string.sharing_message_popup,deal));

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String body = "Please join my deal by clicking on https://www.grousale.com/deal/"+deal;

                //myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
                myIntent.putExtra(Intent.EXTRA_TEXT,body);
                getContext().startActivity(Intent.createChooser(myIntent, "Share deal using"));
            }
        });


    }

    public void recordAudio() throws FileNotFoundException {

        AudioTimer.setBase(SystemClock.elapsedRealtime());
        AudioTimer.start();
        recording = true;

        mic.playAnimation();
        mic.setRepeatCount(LottieDrawable.INFINITE);

        ContentValues values = new ContentValues(4);

        values.put(MediaStore.Audio.Media.TITLE, deal);
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.DISPLAY_NAME,deal + ".mp3");

        //values.put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/Recordings/");

        audiouri = mcontext.getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
        file = mcontext.getContentResolver().openFileDescriptor(audiouri, "w");

        Log.d("File: ",audiouri+" | " + file + " | "+ file.getFileDescriptor());

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setOutputFile(file.getFileDescriptor());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioSamplingRate(16000);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();

        } catch (IOException e) {

            Log.e(LOG_TAG, "prepare() failed");
        }


    }

    private void checkPermissionsAndStart() {
        Dexter.withContext(mcontext).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                if (multiplePermissionsReport.areAllPermissionsGranted()) {

                }


                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {

                    showSettingsDialog();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                permissionToken.continuePermissionRequest();

            }
        }).onSameThread()
                .check();


        if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            try {
                recordAudio();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(mcontext, "Recording didnt work", LENGTH_SHORT).show();
            }
            flag = 1;
            recording=true;
            RecordStatus.setVisibility(View.INVISIBLE);
            AudioTimer.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Recording is Started...", LENGTH_SHORT).show();
        }

    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to work properly. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mcontext.getPackageName(), null);
                intent.setData(uri);
                getOwnerActivity().startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @SuppressLint("ResourceAsColor")
    public void stopAudio() {
        AudioTimer.stop();
        mediaRecorder.stop();
        mediaRecorder.release();


        mediaRecorder = null;

        uploadAudio();

    }

    public void uploadAudio() {

        srAudio = FirebaseStorage.getInstance().getReference().child("UploadAudio");

        Uri uri = audiouri;
        StorageReference filepath = srAudio.child(deal).child("dealAudio"+ deal+".mp3");


        Log.d("audio_link", "path" + filepath);

        Log.d("audio_link", "uri path" + uri);

        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(getContext(), "Saved  Successfully", Toast.LENGTH_SHORT).show();

                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri FileUri) {

                        drAudio.child("audioFileLink").setValue(String.valueOf(FileUri)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Audio Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                }

                                else {
                                    Toast.makeText(getContext(), "Some error occurred, Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                });

            }
        });

    }


    @Override
    public void onClick(View v) {

    }
}

package com.hasanbilgin.instagramclonejava.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hasanbilgin.instagramclonejava.databinding.ActivityUploadBinding;

import java.util.HashMap;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    Uri imagaData;
    private ActivityUploadBinding binding;
    //Bitmap selectedImage;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);
        registerLuncher();
        //firebaseStorage de bi obje olu??tuurp verilerini alm???? oluuruz
        firebaseStorage = FirebaseStorage.getInstance();
        //FirebaseAuth de bi obje olu??tuurp verilerini alm???? oluuruz
        auth = FirebaseAuth.getInstance();
        //firebaseFirestore de bi obje olu??tuurp verilerini alm???? oluuruz
        firebaseFirestore = FirebaseFirestore.getInstance();
        //olu??turdu??umuzda store bo?? bir alan veriyo sitede storage deki alan?? veriyo diyebiliriz
        storageReference = firebaseStorage.getReference();
    }


    public void uploadOnClickButton(View view) {
        if (imagaData != null) {
            //child() ??ocuk yani klas??r diyebiliriz
            //putFile() ise g??rsel datas??n??n kendisi
            //storageReference ebeveyn dir
            //burda images klas??r??ne image.jpg ad?? ile g??rsel dosyas??n?? yani imagaData datas?? ile kaydeder
            //resimleri herseferinde child("images/image.jpg") image.jpg ile kaydedersek en son resim kay??t hep bi ??ncekini silerondan dolay??
            //alttaki gibi random id olu??turur nerdeyse 36 haneli
            UUID uuid = UUID.randomUUID();
            String imageName = "images/" + uuid + ".jpg";
            storageReference.child(imageName).putFile(imagaData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                //ba??ar??l?? oldu??unda
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Download url
                    //olan imgaeName stringini verip url vericektir
                    StorageReference newStorageReference = firebaseStorage.getReference(imageName);
                    //indirirken olan d??n???? i??in ba??ar??l?? oldu??unda
                    newStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            String comment = binding.commentEditText.getText().toString();
                            //emailini almak i??in olan kullan??c??y?? ald??k
                            FirebaseUser user = auth.getCurrentUser();
                            String email = user.getEmail();
                            //firestorede ky ve value oldu??u i??in Strign , Object ald??k
                            HashMap<String, Object> postData = new HashMap<>();
                            postData.put("useremail", email);
                            postData.put("downloadurl", downloadUrl);
                            postData.put("comment", comment);
                            // FieldValue.serverTimestamp()     serverdeki direk zaman?? al??cakt??r
                            postData.put("date", FieldValue.serverTimestamp());

                            //firebaseFirestore ekleme yaparken
                            firebaseFirestore.collection("Posts").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override

                                public void onSuccess(DocumentReference documentReference) {
                                    Intent intent = new Intent(UploadActivity.this, FeedActivity.class);
                                    //t??m arka planda ??al????an intent yani activityleri bu activite dahil bo??alt??cakt??r yani temizlicektir
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                //ba??ar??s??z oldu??unda yani hatal?? bir i??lemde
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void imageViewOnClick(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //ask permission
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            } else {
                //ask permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

            }

        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);
        }


    }

    public void registerLuncher() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intentFromResult = result.getData();
                    if (intentFromResult != null) {
                        imagaData = intentFromResult.getData();
                        binding.imageView.setImageURI(imagaData);


                        //mitmap iserse not halinde dursun
                        /*
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                ImageDecoder.Source source = ImageDecoder.createSource(UploadActivity.this.getContentResolver(), imagaData);
                                selectedImage = ImageDecoder.decodeBitmap(source);
                                binding.imageView.setImageBitmap(selectedImage);
                            } else {
                                selectedImage = MediaStore.Images.Media.getBitmap(UploadActivity.this.getContentResolver(), imagaData);
                                binding.imageView.setImageBitmap(selectedImage);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        */

                    }
                }
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                } else {
                    Toast.makeText(UploadActivity.this, "Permission Needed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
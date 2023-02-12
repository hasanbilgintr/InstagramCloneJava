package com.hasanbilgin.instagramclonejava.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hasanbilgin.instagramclonejava.R;
import com.hasanbilgin.instagramclonejava.adaper.PostAdapter;
import com.hasanbilgin.instagramclonejava.databinding.ActivityFeedBinding;
import com.hasanbilgin.instagramclonejava.model.PostModel;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<PostModel> postArrayList;
    private ActivityFeedBinding binding;
    PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        postArrayList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        getData();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postArrayList,this);
        binding.recyclerView.setAdapter(postAdapter);

    }

    public void getData() {
        //bir kerede liste alınabilir yada liste güncellendiği anda değişiklik yapacak listede alınabilir
        //yapılan güncelleme olduğunda  yenilenen lsite alıyoruz
//        DocumentReference documentReference=firebaseFirestore.collection("Posts").document("sdfsdf");
//        CollectionReference collectionReference=firebaseFirestore.collection("Posts");
        //döcumenti uuid verdiğimiz için yani rasgele ordan ilerlemedik

//        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {

        //usermail kolonunun sadece hasanbilgintr@gmail.com ise getir where username='hasanbilgintr@gmail.com' gibi
//        firebaseFirestore.collection("Posts").whereEqualTo("usermail","hasanbilgintr@gmail.com").addSnapshotListener(new EventListener<QuerySnapshot>() {
        //dizisi şunları içerenlerden araştırabilirsin
//        firebaseFirestore.collection("Posts").whereArrayContains().addSnapshotListener(new EventListener<QuerySnapshot>() {
        //5ten büyük olanlar gelicektir vs.. whereLessThan daha küçük / whereArraycontainsAny daha büyük /  whereArraycontains şunu kapsayan / wherentoEqualTo buna eşit olmayan /
//        firebaseFirestore.collection("Posts").whereGreaterThan("number",5).addSnapshotListener(new EventListener<QuerySnapshot>() {
        //DESCENDING İSE büyükten küçüğe sıralması / ASCENDING ise küçükten büyüğüe sıralar
        firebaseFirestore.collection("Posts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(FeedActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                if (value != null) {

                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        String userEmail = (String) data.get("useremail");
                        String comment = (String) data.get("comment");
                        String downloadUrl = (String) data.get("downloadurl");
                        //System.out.println(comment);
                        PostModel postModel = new PostModel(userEmail, comment, downloadUrl);
                        postArrayList.add(postModel);
                    }
                    //bu adapterdeki veriyi recyclerviewdekine  veri geldi yansıt diye uyarmış oluyor
                    // bu arada addSnapshotListener bu metot ilkte okumuyo snra okuyo bu şekilde ve her güncellendiğinde yeniliyo otomatik
                    postAdapter.notifyDataSetChanged();
                }

            }
        });

    }

    //bağladık
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_post:
                Intent intentToUpload = new Intent(FeedActivity.this, UploadActivity.class);
                startActivity(intentToUpload);
                finish();
                break;

            case R.id.signout:
                //signout
                //çıkış yaptırır
                auth.signOut();
                Intent intentToMain = new Intent(FeedActivity.this, MainActivity.class);
                startActivity(intentToMain);
                finish();


            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
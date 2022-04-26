package com.bruno13palhano.ssapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bruno13palhano.ssapp.MainActivity;
import com.bruno13palhano.ssapp.R;
import com.bruno13palhano.ssapp.SSUtil;
import com.bruno13palhano.ssapp.adapters.TestPagingAdapter;
import com.bruno13palhano.ssapp.data.Player;
import com.bruno13palhano.ssapp.viewmodels.PlayerViewModel;
import com.bruno13palhano.ssapp.viewmodels.TestViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SettingsFragment extends Fragment {
    private Button signOut;

    private FirebaseAuth auth;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    private Bitmap image = null;
    private String imageUrlFromStore;
    ImageView profileImage;

    List<Player> playersList;

    TestPagingAdapter testAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.label_settings_fragment);


        MainActivity mainActivity = (MainActivity) getActivity();
        if(mainActivity != null){
            mainActivity.disableUpButton();
        }

        setHasOptionsMenu(true);

        signOut = view.findViewById(R.id.button_sign_out);

        auth = FirebaseAuth.getInstance();

        profileImage = view.findViewById(R.id.profile_image);

        //teste

        TestViewModel viewModel = new ViewModelProvider(this)
                .get(TestViewModel.class);
        testAdapter = new TestPagingAdapter(new TestPagingAdapter.PlayerComparator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView testRecycler = view.findViewById(R.id.test_recycler);
        testRecycler.setLayoutManager(linearLayoutManager);

        testRecycler.setAdapter(testAdapter);

        PlayerViewModel playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        playerViewModel.getAllPlayers().observe(getViewLifecycleOwner(), players -> {
            playersList = players;

            processInBg(playersList);

            viewModel.getLiveData().observe(getViewLifecycleOwner(), playerPagingData -> {
                testAdapter.submitData(getViewLifecycleOwner().getLifecycle(), playerPagingData);
            });
        });

        //teste

//        getProfileImage(profileImage);



        return view;
    }
    public interface OnProcessedListener{
        void onProcessed(Map<Long, Bitmap> listBitmaps);
    }

    private void processInBg(final List<Player> players){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        final OnProcessedListener listener = new OnProcessedListener(){
            @Override
            public void onProcessed(Map<Long, Bitmap> listBitmaps){
                // Use the handler so we're not trying to update the UI from the bg thread
                handler.post(new Runnable(){
                    @Override
                    public void run(){
                        testAdapter.setBitmaps(listBitmaps);
                        testAdapter.notifyDataSetChanged();
                        executor.shutdown();
                    }
                });
            }
        };

        Runnable backgroundRunnable = new Runnable(){
            @Override
            public void run(){
                try {
                    Map<Long, Bitmap> listBitmaps = new HashMap<>();

                    final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    for (int i = 0; i < players.size(); i++) {
                        String imageUri = players.get(i).getPlayerImageUri();
                        requireActivity().getContentResolver().takePersistableUriPermission(Uri.parse(imageUri), takeFlags);
                        listBitmaps.put(players.get(i).getPlayerId(), SSUtil.decodeUri(requireActivity(), Uri.parse(imageUri), 56));
                    }

                    listener.onProcessed(listBitmaps);

                } catch (FileNotFoundException | IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        };

        executor.execute(backgroundRunnable);
    }

    private void getProfileImage(ImageView imageView){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference().child("images/"+
                auth.getCurrentUser().getUid().concat("_profile_image.png"));

//        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child()
//        //that child is up to you
//        storageReference.putFile(imageUri).addOnCompleteListener(task ->
//                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
//                    String url = uri.toString();
//                }));

        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println("caminho da uri: "+uri.toString());
                imageUrlFromStore = uri.toString();

                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());

                executor.execute(() -> {
                    try{
                        // TODO: 17/04/2022  mudar a url par aimage do perfil do jogador...
                        URL imageUrl = new URL(imageUrlFromStore);
                        InputStream in = imageUrl.openStream();

                        image = BitmapFactory.decodeStream(in);

                        handler.post( () -> {
                            imageView.setImageBitmap(image);
                        });

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void onStart(){
        super.onStart();

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = auth.getCurrentUser();

                if(currentUser != null){
                    AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            System.out.println("sign out negada");
                        }
                    });
                }
            }
        });
    }
}
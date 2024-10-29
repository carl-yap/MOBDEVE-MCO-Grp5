package com.mobdeve.s21.yap.carl.mco2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class AlbumViewModel extends ViewModel {
    private final MutableLiveData<List<Album>> albums;

    public AlbumViewModel() {
        albums = new MutableLiveData<>();
        albums.setValue(new ArrayList<>());
        loadTestData();
    }

    public LiveData<List<Album>> getAlbumList() {
        return albums;
    }

    public void addAlbum(Album a) {
        List<Album> currentList = albums.getValue();
        if (currentList != null) {
            currentList.add(a);
            albums.setValue(currentList);
        }
    }

    // Add more methods as needed

    private void loadTestData() {
        List<Image> dummy = new ArrayList<>();
        for (int i = 0; i < 4; i++) { dummy.add(new Image("test", "test")); }

        List<Album> dummyAlbum = new ArrayList<>();
        dummyAlbum.add(new Album("Album A", dummy));
        dummyAlbum.add(new Album("Album B", dummy));
        albums.setValue(dummyAlbum);
    }
}

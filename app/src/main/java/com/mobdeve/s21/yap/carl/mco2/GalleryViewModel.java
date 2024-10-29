package com.mobdeve.s21.yap.carl.mco2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class GalleryViewModel extends ViewModel {
    private final MutableLiveData<List<Image>> images;

    public GalleryViewModel() {
        images = new MutableLiveData<>();
        images.setValue(new ArrayList<>());
        loadTestData();
    }

    public LiveData<List<Image>> getImageList() {
        return images;
    }

    public void addImage(Image i) {
        List<Image> currentList = images.getValue();
        assert currentList != null;
        if (!currentList.isEmpty()) {
            currentList.add(i);
            images.setValue(currentList);
        }
    }

    // Add more methods as needed

    private void loadTestData() {
        List<Image> dummy = new ArrayList<>();
        for (int i = 0; i < 6; i++) { dummy.add(new Image("test", "test")); }
        images.setValue(dummy);
    }
}

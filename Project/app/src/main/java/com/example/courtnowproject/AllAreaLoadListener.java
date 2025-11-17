package com.example.courtnowproject;

import java.util.List;

public interface AllAreaLoadListener {
    void onAllAreaLoadSuccess(List<String> areaNameList);
    void onAllAreaLoadFailed(String message);


}

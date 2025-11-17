package com.example.courtnowproject;

import java.util.List;

public interface ComplexLoadListener {
    void onComplexLoadSuccess(List<Complex> complexList );
    void onComplexLoadFailed(String message);
}

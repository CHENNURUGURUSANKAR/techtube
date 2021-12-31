package com.guru149companies.telugutechtube;

public interface OnK4LVideoListener {
    void onTrimStarted();

    void onError(String message);

    void onVideoPrepared();
}

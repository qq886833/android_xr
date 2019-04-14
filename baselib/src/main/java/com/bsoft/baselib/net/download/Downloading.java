package com.bsoft.baselib.net.download;

import androidx.annotation.NonNull;

public class Downloading {
    private long currentSize;
    private long totalSize;
    private boolean done;
    private String filePath;

    public Downloading(long totalSize, @NonNull String filePath) {
        this.totalSize = totalSize;
        this.filePath = filePath;
    }

    public void set(long currentSize, boolean done) {
        this.currentSize = currentSize;
        this.done = done;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

package de.urkallinger.kallingapp.data;

public interface KallingCallback<T> {
    void onFailure(Exception e);

    void onSuccess(T data);
}
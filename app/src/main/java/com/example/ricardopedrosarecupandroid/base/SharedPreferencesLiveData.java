package com.example.ricardopedrosarecupandroid.base;

import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;

public abstract class SharedPreferencesLiveData<T> extends LiveData<T> {

    protected final SharedPreferences sharedPreferences;
    private final String key;
    private final T defaultValue;

    private SharedPreferences.OnSharedPreferenceChangeListener
            onSharedPreferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                      String key) {
                    if (TextUtils.equals(key, SharedPreferencesLiveData.this.key)) {
                        // Se establece el nuevo valor en el LiveData.
                        setValue(getValueFromPreferences(key, defaultValue));
                    }
                }
            };

    SharedPreferencesLiveData(SharedPreferences sharedPreferences, String key,
                              T defaultValue) {
        this.sharedPreferences = sharedPreferences;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    // Método abstracto para obtener el valor de una determinada preferencia.
    // Su implementación concreta usará el método getTipo() adecuado del
    // objeto SharedPreferencia dependiendo de qué tipo concreto sea T.
    protected abstract T getValueFromPreferences(String key, T defaultValue);

    // Cuando se pasa a tener algún observador se establece el valor. De esta manera
    // se entrega el valor directamente, sin que se tenga que producir un cambio.
    // Además se registra el listener de observación de cambios.
    @Override
    protected void onActive() {
        super.onActive();
        setValue(getValueFromPreferences(key, defaultValue));
        sharedPreferences.registerOnSharedPreferenceChangeListener(
                onSharedPreferenceChangeListener);
    }

    // Cuando se dejan de tener observadores, se quita el listener del registro
    @Override
    protected void onInactive() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(
                onSharedPreferenceChangeListener);
        super.onInactive();
    }

}
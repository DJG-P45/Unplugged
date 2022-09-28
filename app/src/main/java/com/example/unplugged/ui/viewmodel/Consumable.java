package com.example.unplugged.ui.viewmodel;

/*
* This class allows for a value to only be retrieved once.
* This is useful when UI layer experiences configuration changes
* and "cached" data is reloaded from a ViewModel.
* */
public class Consumable<T> {

    private boolean consumed;
    private final T value;

    public Consumable(T value) {
        this.value = value;
        this.consumed = false;
    }

    public boolean notConsumed() {
        return !consumed;
    }

    public T consume() {
        if (consumed) return null;
        consumed = true;
        return value;
    }
}

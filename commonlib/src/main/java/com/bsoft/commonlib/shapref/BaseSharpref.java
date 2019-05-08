package com.bsoft.commonlib.shapref;

import android.content.Context;

public abstract class BaseSharpref {
    protected static final int SHARED_MODE = Context.MODE_PRIVATE;
    private String name;
    private Context context;
    
    
    public BaseSharpref(Context context, String name){
        this.context = context.getApplicationContext();
        this.name = name;
    }

    protected String getString(String key) {
        return getString(key, null);
    }

    protected String getString(String key, String def) {
        return context
                .getSharedPreferences(name, SHARED_MODE)
                .getString(key, def);
    }

    protected boolean setString(String value, String key) {
        return context
                .getSharedPreferences(name, SHARED_MODE)
                .edit()
                .putString(key, value)
                .commit();
    }

    protected boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    protected boolean getBoolean(String key, boolean def) {
        return context
                .getSharedPreferences(name, SHARED_MODE)
                .getBoolean(key, def);
    }

    protected boolean setBoolean(boolean value, String key) {
        return context
                .getSharedPreferences(name, SHARED_MODE)
                .edit()
                .putBoolean(key, value)
                .commit();
    }
    protected long getLong(String key) {
        return getLong(key, 0);
    }

    protected long getLong(String key, long def) {
        return context
                .getSharedPreferences(name, SHARED_MODE)
                .getLong(key, def);
    }

    protected boolean setLong(long value, String key) {
        return context
                .getSharedPreferences(name, SHARED_MODE)
                .edit()
                .putLong(key, value)
                .commit();
    }
    protected int getInt(String key) {
        return getInt(key, 0);
    }

    protected int getInt(String key, int def) {
        return context
                .getSharedPreferences(name, SHARED_MODE)
                .getInt(key, def);
    }

    protected boolean setInt(int value, String key) {
        return context
                .getSharedPreferences(name, SHARED_MODE)
                .edit()
                .putInt(key, value)
                .commit();
    }
}

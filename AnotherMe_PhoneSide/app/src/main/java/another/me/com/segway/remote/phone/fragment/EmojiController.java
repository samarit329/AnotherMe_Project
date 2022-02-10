<<<<<<< HEAD
package another.me.com.segway.remote.phone.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import another.me.com.segway.remote.phone.fragment.base.PreferenceFragment;
import another.me.com.segway.remote.phone.util.CommandStringFactory;
import another.me.com.segway.remote.phone.service.ConnectionService;
import another.me.com.segway.remote.phone.R;

public class EmojiController extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "SettingsFragment";

    //public static final String KEY_VOICE = "voice_recognition";
    public static final String KEY_EMOJI = "emoji";

   @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        boolean enabled;
        switch (key) {


            case KEY_EMOJI:
                enabled = sharedPreferences.getBoolean(key, false);
                this.toggleEmoji(enabled);
                break;
        }
    }



    public void toggleEmoji(boolean enabled) {
        Log.d(TAG, "toggleEmoji called with " + enabled);
        String[] message = {"settings", KEY_EMOJI, String.valueOf(enabled)};
        ConnectionService.getInstance().send(CommandStringFactory.getStringMessage(message));
    }



}
=======
package another.me.com.segway.remote.phone.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import another.me.com.segway.remote.phone.fragment.base.PreferenceFragment;
import another.me.com.segway.remote.phone.util.CommandStringFactory;
import another.me.com.segway.remote.phone.service.ConnectionService;
import another.me.com.segway.remote.phone.R;

public class EmojiController extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "SettingsFragment";

    //public static final String KEY_VOICE = "voice_recognition";
    public static final String KEY_EMOJI = "emoji";

   @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        boolean enabled;
        switch (key) {


            case KEY_EMOJI:
                enabled = sharedPreferences.getBoolean(key, false);
                this.toggleEmoji(enabled);
                break;
        }
    }



    public void toggleEmoji(boolean enabled) {
        Log.d(TAG, "toggleEmoji called with " + enabled);
        String[] message = {"settings", KEY_EMOJI, String.valueOf(enabled)};
        ConnectionService.getInstance().send(CommandStringFactory.getStringMessage(message));
    }



}
>>>>>>> 6a06341ee2df2f62f8c6eeca66a5de7f86a32c3b

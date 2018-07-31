package tgms.ttt;

import android.os.Bundle;
import android.os.Handler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import tgms.ttt.PlatformInterfaces.Platform;
import tgms.ttt.TicTacToe;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler h = new Handler();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        Platform.FONT_SIZE = 48;
        initialize(new TicTacToe(new AndroidUtils(getContext(), h)), config);
    }
}

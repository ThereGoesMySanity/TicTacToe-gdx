package tgms.ttt;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.widget.EditText;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import tgms.ttt.Net.Socket.SocketClient;
import tgms.ttt.Net.Socket.SocketServer;
import tgms.ttt.PlatformInterfaces.Platform;

import static tgms.ttt.Net.Connection.DEFAULT_PORT;

class AndroidUtils extends Platform {
    @Override
    public BitmapFont getFont() {
        if (font == null) {
            Gdx.app.debug("test", Gdx.files.internal("fonts/default.ttf").exists() + " font");
            FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/default.ttf"));
            FreeTypeFontParameter param = new FreeTypeFontParameter();
            param.size = FONT_SIZE;
            param.flip = true;
            font = gen.generateFont(param);
            gen.dispose();
        }
        return font;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    AndroidUtils(Context c) {
        thread = (r) -> new Thread(r).start();

        AlertDialog.Builder ab =
                new AlertDialog.Builder(c, android.R.style.Theme_Material_Dialog_Alert);
        ab.setTitle("Online");
        ab.setMessage("Do you want to host or connect?");
        AlertDialog.Builder ip =
                new AlertDialog.Builder(c, android.R.style.Theme_Material_Dialog_Alert);
        ip.setTitle("Choose IP to connect to");
        final EditText input = new EditText(c);
        ip.setView(input);
        AlertDialog.Builder uname =
                new AlertDialog.Builder(c, android.R.style.Theme_Material_Dialog_Alert);
        ip.setTitle("Choose IP to connect to");
        final EditText name = new EditText(c);
        ip.setView(input);

        online = () -> {
            AtomicBoolean host = new AtomicBoolean(false);
            ab.setPositiveButton("Host", (d, i) -> host.set(true));
            ab.setNegativeButton("Connect", (d, i) -> host.set(false));
            ab.show();

            AtomicReference<String> n = new AtomicReference<>("");
            uname.setPositiveButton("OK", (d, i) -> n.set(name.getText().toString()));
            uname.setNegativeButton("Cancel", (dialog, i) -> dialog.cancel());
            uname.show();
            String username = n.get();
            if (!username.isEmpty()) {
                if (host.get()) {
                    return new SocketServer(username, DEFAULT_PORT);
                } else {
                    AtomicReference<String> s = new AtomicReference<>("");
                    ip.setPositiveButton("OK", (d, i) -> s.set(input.getText().toString()));
                    ip.setNegativeButton("Cancel", ((dialog, i) -> dialog.cancel()));
                    ip.show();
                    String ipaddr = s.get();
                    if (!ipaddr.isEmpty()) {
                        return new SocketClient(username, ipaddr, DEFAULT_PORT);
                    }
                }
            }
            return null;
        };
    }
}

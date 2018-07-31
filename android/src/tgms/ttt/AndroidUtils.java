package tgms.ttt;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.widget.EditText;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import tgms.ttt.Net.Connection;
import tgms.ttt.Net.Socket.SocketClient;
import tgms.ttt.Net.Socket.SocketServer;
import tgms.ttt.PlatformInterfaces.Platform;

import static tgms.ttt.Net.Socket.SocketConnection.DEFAULT_PORT;

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
    AndroidUtils(Context c, Handler h) {
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
        uname.setTitle("Set username");
        final EditText name = new EditText(c);
        uname.setView(name);

        online = () -> {
            AtomicReference<Connection> ref = new AtomicReference<>(null);
            AtomicBoolean done = new AtomicBoolean(false);
            uname.setPositiveButton("OK", (d, i) -> {
                String username = name.getText().toString();
                if (!username.isEmpty()) {
                    ab.show();
                } else {
                    done.set(true);
                }
            });
            uname.setNegativeButton("Cancel", (d, i) -> done.set(true));
            ab.setPositiveButton("Host", (d, i) -> {
                ref.set(new SocketServer(name.getText().toString(), DEFAULT_PORT));
                done.set(true);
            });
            ab.setNegativeButton("Connect", (d, i) -> ip.show());
            ip.setPositiveButton("OK", (d, i) -> {
                String ipaddr = input.getText().toString();
                if (!ipaddr.isEmpty()) {
                    ref.set(new SocketClient(name.getText().toString(), ipaddr, DEFAULT_PORT));
                }
                done.set(true);
            });
            ip.setNegativeButton("Cancel", ((dialog, i) -> {
                dialog.cancel();
                done.set(true);
            }));
            synchronized (h) {
                h.post(uname::show);
                while (!done.get()) {
                    try {
                        h.wait(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return ref.get();
            }
        };
    }
}

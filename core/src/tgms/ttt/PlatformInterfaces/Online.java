package tgms.ttt.PlatformInterfaces;

import java.io.IOException;

import tgms.ttt.Net.Connection;

public interface Online extends Supportable {
    Connection getConnection();
}

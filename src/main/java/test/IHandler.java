package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IHandler {
    public void handle(InputStream inClient, OutputStream outClient) throws IOException, ClassNotFoundException, Exception;
}

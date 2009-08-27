package ro.gagarin.config;

import java.io.File;

public interface FileChangeObserver {
    void fileChanged(File file);
}

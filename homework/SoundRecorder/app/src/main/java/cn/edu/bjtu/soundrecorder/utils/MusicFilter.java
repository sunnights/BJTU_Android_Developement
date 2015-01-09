package cn.edu.bjtu.soundrecorder.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by Jake on 2015/1/10.
 */
public class MusicFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String filename) {
        return (filename.endsWith(".amr") | filename.endsWith(".mp3"));
    }
}

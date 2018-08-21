package com.eric.chapter03;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
@FunctionalInterface
public interface BufferedReaderProcessor {

    String processFile(BufferedReader b) throws IOException;
}

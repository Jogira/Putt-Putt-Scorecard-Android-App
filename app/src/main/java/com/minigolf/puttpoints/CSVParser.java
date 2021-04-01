package com.minigolf.puttpoints;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class CSVParser {
    InputStream stream;

    public CSVParser(InputStream inputStream) {
        this.stream = inputStream;
    }

    public List reading() {

        List resultList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                resultList.add(row);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
        return resultList;

    }


}

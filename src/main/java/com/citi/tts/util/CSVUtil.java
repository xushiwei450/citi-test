package com.citi.tts.util;

import com.citi.tts.model.HolidayModel;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import de.siegmar.fastcsv.writer.CsvWriter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {



    public static List<HolidayModel> csvReadOperation() throws IOException {
        File file = new File("./data/holidayInfo.csv");

        CsvReader csvReader = new CsvReader();
        CsvContainer csv = csvReader.read(file, StandardCharsets.UTF_8);
        List<HolidayModel> list = new ArrayList<>();
        for (CsvRow row : csv.getRows()) {
            if (row.getOriginalLineNumber() != 1) {
                HolidayModel resultInfo = new HolidayModel();
                resultInfo.setCountryCode(row.getField(0));
                resultInfo.setCountryDesc(row.getField(1));
                resultInfo.setHolidayDate(row.getField(2));
                resultInfo.setHolidayName(row.getField(3));
                list.add(resultInfo);
            }
        }

        return list;
    }


    public static void csvWriteOperation(List<String[]> inputList) throws IOException {
        List<String[]> list = new ArrayList<>();
        String[] title = new String[4];
        title[0] = "countryCode";
        title[1] = "countryDesc";
        title[2] = "holidayDate";
        title[3] = "holidayName";
        list.add(title);
        if(inputList!=null&&inputList.size()>0){
            for(String[] data : inputList){
                list.add(data);
            }
        }
        File file = new File("./data/holidayInfo.csv");
        CsvWriter writer = new CsvWriter();
        writer.write(file,StandardCharsets.UTF_8,list);

    }
}

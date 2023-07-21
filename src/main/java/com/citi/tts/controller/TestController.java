package com.citi.tts.controller;

import com.citi.tts.model.CheckHolidayModel;
import com.citi.tts.model.HolidayModel;
import com.citi.tts.model.NextHolidayModel;
import com.citi.tts.util.CSVUtil;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

@RestController
public class TestController {
    

    @PostMapping(value = "/add/holiday")
    @ResponseBody
    public CommonResponse addHoliday(@RequestBody AddHolidayVO addHolidayVO) throws Exception{

        List<HolidayModel> holidayModels =  CSVUtil.csvReadOperation();
        holidayModels.addAll(addHolidayVO.getHolidayInfoList());
        List<String[]> inputList = new ArrayList<>();
        for(HolidayModel model : holidayModels){
            String[] data = new String[4];
            data[0] = model.getCountryCode();
            data[1] = model.getCountryDesc();
            data[2] = model.getHolidayDate();
            data[3] = model.getHolidayName();
            inputList.add(data);
        }
        CSVUtil.csvWriteOperation(inputList);
        return CommonResponse.ok();
    }

    @PostMapping(value = "/update/holiday")
    @ResponseBody
    public CommonResponse updateHoliday(@RequestBody AddHolidayVO addHolidayVO) throws Exception{

        List<HolidayModel> holidayModels =  CSVUtil.csvReadOperation();
        HashMap<String,HolidayModel> holidayModelsMap = new HashMap<>();

        for(HolidayModel holidayModel : holidayModels){
            holidayModelsMap.put(holidayModel.getCountryCode()+holidayModel.getHolidayDate(),holidayModel);
        }

        for(HolidayModel holidayModel : addHolidayVO.getHolidayInfoList()){
            if(holidayModelsMap.get(holidayModel.getCountryCode()+holidayModel.getHolidayDate())!=null){
                holidayModelsMap.put(holidayModel.getCountryCode()+holidayModel.getHolidayDate(),holidayModel);
            }
        }
        List<HolidayModel> lastHolidayModels =  new ArrayList<>();
        for(Map.Entry<String,HolidayModel> entry : holidayModelsMap.entrySet()){
            lastHolidayModels.add(entry.getValue());
        }

        List<String[]> inputList = new ArrayList<>();
        for(HolidayModel model : lastHolidayModels){
            String[] data = new String[4];
            data[0] = model.getCountryCode();
            data[1] = model.getCountryDesc();
            data[2] = model.getHolidayDate();
            data[3] = model.getHolidayName();
            inputList.add(data);
        }
        CSVUtil.csvWriteOperation(inputList);
        return CommonResponse.ok();
    }

    @PostMapping(value = "/remove/holiday")
    @ResponseBody
    public CommonResponse removeHoliday(@RequestBody AddHolidayVO addHolidayVO) throws Exception{

        List<HolidayModel> holidayModels =  CSVUtil.csvReadOperation();
        HashMap<String,HolidayModel> holidayModelsMap = new HashMap<>();

        for(HolidayModel holidayModel : holidayModels){
            holidayModelsMap.put(holidayModel.getCountryCode()+holidayModel.getHolidayDate(),holidayModel);
        }

        for(HolidayModel holidayModel : addHolidayVO.getHolidayInfoList()){
            if(holidayModelsMap.get(holidayModel.getCountryCode()+holidayModel.getHolidayDate())!=null){
                holidayModelsMap.remove(holidayModel.getCountryCode()+holidayModel.getHolidayDate());
            }
        }
        List<HolidayModel> lastHolidayModels =  new ArrayList<>();
        for(Map.Entry<String,HolidayModel> entry : holidayModelsMap.entrySet()){
            lastHolidayModels.add(entry.getValue());
        }

        List<String[]> inputList = new ArrayList<>();
        for(HolidayModel model : lastHolidayModels){
            String[] data = new String[4];
            data[0] = model.getCountryCode();
            data[1] = model.getCountryDesc();
            data[2] = model.getHolidayDate();
            data[3] = model.getHolidayName();
            inputList.add(data);
        }
        CSVUtil.csvWriteOperation(inputList);
        return CommonResponse.ok();
    }

    @PostMapping(value = "/check/holiday")
    @ResponseBody
    public CommonResponse checkHoliday(@RequestBody CheckHolidayModel model) throws Exception{

        List<HolidayModel> holidayModels =  CSVUtil.csvReadOperation();
        HashMap<String,HolidayModel> holidayModelsMap = new HashMap<>();

        for(HolidayModel holidayModel : holidayModels){
            holidayModelsMap.put(holidayModel.getCountryCode()+holidayModel.getHolidayDate(),holidayModel);
        }
        if(holidayModelsMap.get(model.getCountryCode()+model.getHolidayDate())==null){
            return CommonResponse.ok("false","it is not a holiday");
        }

        return CommonResponse.ok("it is a holiday");
    }

    @PostMapping(value = "/next/holiday")
    @ResponseBody
    public CommonResponse nextHoliday(@RequestBody NextHolidayModel model) throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Integer now = Integer.parseInt(sdf.format(new Date()));
        List<HolidayModel> holidayModels =  CSVUtil.csvReadOperation();
        HashMap<Integer,HolidayModel> holidayModelsMap = new HashMap<>();
        List<Integer> dataList = new ArrayList<>();
        for(HolidayModel holidayModel : holidayModels){
            if(holidayModel.getCountryCode().equals(model.getCountryCode())){
                String time = holidayModel.getHolidayDate().replaceAll("-","");
                Integer dateTime = Integer.parseInt(time);
                dataList.add(dateTime);
                holidayModelsMap.put(dateTime,holidayModel);
            }
        }
        dataList.sort((o1,o2)->{return (o1-o2);});
        Integer key = null;
        for(Integer dateTime : dataList){
            if(dateTime>now){
                key = dateTime;
                break;
            }
        }
        if(key==null){
            return CommonResponse.ok("no holiday after today");
        }

        return CommonResponse.ok(holidayModelsMap.get(key));
    }


    @PostMapping(value = "/next/year/holiday")
    @ResponseBody
    public CommonResponse nextYearHoliday(@RequestBody NextHolidayModel model) throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Integer now = Integer.parseInt(sdf.format(new Date()));
        Integer next = now+1;
        List<HolidayModel> holidayModels =  CSVUtil.csvReadOperation();
        List<HolidayModel> nextHoliday = new ArrayList<>();
        for(HolidayModel holidayModel : holidayModels){
            if(holidayModel.getCountryCode().equals(model.getCountryCode())){
                String time = holidayModel.getHolidayDate().substring(0,4);
                Integer dateTime = Integer.parseInt(time);
                if(dateTime.equals(next)){
                    nextHoliday.add(holidayModel);
                }
            }
        }

        return CommonResponse.ok(nextHoliday);
    }

}

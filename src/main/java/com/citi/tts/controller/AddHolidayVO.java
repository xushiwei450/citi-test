package com.citi.tts.controller;

import com.citi.tts.model.HolidayModel;
import lombok.Data;

import java.util.List;

@Data
public class AddHolidayVO {

  private List<HolidayModel> holidayInfoList;
}

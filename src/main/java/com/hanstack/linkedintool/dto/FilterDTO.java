package com.hanstack.linkedintool.dto;


import com.hanstack.linkedintool.constant.ToolbarEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openqa.selenium.Cookie;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterDTO {
    private ToolbarEnum filterBarGrouping;
    private String globalNavSearch;
    private List<String> lstConnections;
    private List<String> lstConnectionsOf;
    private List<String> lstFollowersOf;
    private List<String> lstLocations;
    private List<String> lstTalksAbout;
    private List<String> lstCurrentCompany;
    private List<String> lstPastCompany;
    private List<String> lstSchool;
    private List<String> lstIndustry;
    private List<String> lstProfileLanguage;
    private List<String> lstOpenTo;
    private List<String> lstServiceCategories;


}

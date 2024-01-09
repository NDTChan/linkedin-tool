package com.hanstack.linkedintool.model;


import com.hanstack.linkedintool.constant.ToolbarEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterModel {
    private ToolbarEnum filterBarGrouping;
    private String globalNavSearch;
}

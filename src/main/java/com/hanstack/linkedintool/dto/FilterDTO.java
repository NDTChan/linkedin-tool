package com.hanstack.linkedintool.dto;


import com.hanstack.linkedintool.constant.ToolbarEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterDTO {
    private ToolbarEnum filterBarGrouping;
    private String globalNavSearch;
}

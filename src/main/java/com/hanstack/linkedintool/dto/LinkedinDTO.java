package com.hanstack.linkedintool.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkedinDTO {
    private FilterDTO filterDTO;
    private UploadFileDTO uploadFileDTO;
}

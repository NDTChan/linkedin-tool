package com.hanstack.linkedintool.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
//    private String username;
//    private String password;
//    private boolean cookie;
    private MultipartFile cookieFile;
}

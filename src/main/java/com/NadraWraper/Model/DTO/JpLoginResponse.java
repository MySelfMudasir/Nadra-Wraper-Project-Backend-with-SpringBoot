package com.NadraWraper.Model.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JpLoginResponse {
    private String statusCode;
    private String message;
    private String token;
    private String tokenExpiry;
}

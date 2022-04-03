package com.ituwei.myblog.payload.request;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CommentRequest {
    @NotBlank
    @Size(min=10,message = "min of ten chars")
    private String body;
}

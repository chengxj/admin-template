package com.edgar.module.sys.service;

import lombok.Data;

@Data
public class PasswordCommand {

        private String retypepassword;
        
        private String newpassword;
        
        private String oldpassword;
        
        private int userId;
}

package org.clx.library.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LibraryResponse {

        private String responseCode;
        private String responseMessage;
        private AccountInfo accountInfo;

}

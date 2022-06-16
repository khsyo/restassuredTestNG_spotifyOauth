package com.spotify.oauth2.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter @Setter
@Builder
@Jacksonized
public class ErrorRoot {

    private Error error;
}

package kr.co.igns.framework.user;

import eu.bitwalker.useragentutils.*;
import lombok.Data;

@Data
public class MDCLoginUser {
    private IgnsSessionUser sessionUser;

    private UserAgent userAgent;

    private BrowserType browserType;

    private RenderingEngine renderingEngine;

    private DeviceType deviceType;

    private Manufacturer manufacturer;

}

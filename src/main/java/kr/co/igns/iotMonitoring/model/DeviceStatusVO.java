package kr.co.igns.iotMonitoring.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DeviceStatusVO {
    private String deviceId;
    private String deviceNm;
    private String uniqueID;
    private boolean running;
}

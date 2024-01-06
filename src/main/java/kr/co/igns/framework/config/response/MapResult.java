package kr.co.igns.framework.config.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MapResult<String, Object> extends CommonResult {
    private Map<String, Object> map;
}
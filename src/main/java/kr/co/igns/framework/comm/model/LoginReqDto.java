package kr.co.igns.framework.comm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import io.swagger.annotations.ApiModelProperty;
import kr.co.igns.framework.utils.etc.BaseTime;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Data
public class LoginReqDto {
	@NotNull
    private String userId;
	@NotNull
    private String userPw;
	@NotNull
    private String domainCd;
}

package kr.co.igns.framework.comm.model;

import lombok.*;

@Data
public class UserCertVO {
	private String userId;
	private String password;
	private String name;
	private String roleName;
}

package com.example.zzan.mypage.dto;

import com.example.zzan.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MypageChangeDto {


	private String imgUrl;
	private String username;


	public MypageChangeDto(User myPage) {
		this.imgUrl= myPage.getImg();
		this.username= myPage.getUsername();
	}

}

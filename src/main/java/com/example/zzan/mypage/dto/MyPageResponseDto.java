package com.example.zzan.mypage.dto;


import com.example.zzan.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyPageResponseDto {

	private String imgUrl;
	private String nickname;

	public MyPageResponseDto(User myPage){

		this.imgUrl= myPage.getImg();
		this.nickname= myPage.getNickname();
	}

}

package com.toyproject.bookmanagement.dto.book;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchBookReqDto {
	private int page;
	private String searchValue;
	private List<Integer> categoryIds;
}

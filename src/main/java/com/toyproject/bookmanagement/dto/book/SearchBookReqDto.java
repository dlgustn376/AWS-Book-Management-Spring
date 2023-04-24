package com.toyproject.bookmanagement.dto.book;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchBookReqDto {
	private int page;
	private int categoryId;
	private String searchValue;
}

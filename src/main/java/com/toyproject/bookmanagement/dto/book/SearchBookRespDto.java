package com.toyproject.bookmanagement.dto.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchBookRespDto {
	private int bookId;
	private String bookName;
	private int auhtorId;
	private String authorName;
	private int publisherId;
	private String publisherName;
	private int categoryId;
	private String categoryName;
	private String coverImgUrl;
}

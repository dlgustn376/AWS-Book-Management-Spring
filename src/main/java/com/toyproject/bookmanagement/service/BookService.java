package com.toyproject.bookmanagement.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.toyproject.bookmanagement.dto.book.CategoryRespDto;
import com.toyproject.bookmanagement.dto.book.GetBookRespDto;
import com.toyproject.bookmanagement.dto.book.RentalListRespDto;
import com.toyproject.bookmanagement.dto.book.SearchBookReqDto;
import com.toyproject.bookmanagement.dto.book.SearchBookRespDto;
import com.toyproject.bookmanagement.entity.User;
import com.toyproject.bookmanagement.repository.BookRepository;
import com.toyproject.bookmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
	
	private final BookRepository bookRepository;
	private final UserRepository userRepository;
	
	public GetBookRespDto getBook(int bookId) {
		return bookRepository.getBook(bookId).toGetBookDto();
	}
	
	public Map<String, Object> searchBooks(SearchBookReqDto searchBookReqDto){
		
		List<SearchBookRespDto> list = new ArrayList<>();
		int count = 20;
		int index = (searchBookReqDto.getPage() - 1) * count;
		Map<String, Object> map = new HashMap<>();
		map.put("index", index);
		map.put("categoryIds", searchBookReqDto.getCategoryIds());
		map.put("searchValue", searchBookReqDto.getSearchValue());
		
		bookRepository.searchBooks(map).forEach(book ->{
			list.add(book.toDto());
		});;
		
		int totalCount = bookRepository.getTotalCount(map);

		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("totalCount", totalCount);
		responseMap.put("bookList", list);
		
		
		return responseMap;
	}
	
	public List<CategoryRespDto> getCategories(){
		List<CategoryRespDto> list = new ArrayList<>();
		
		bookRepository.getCategories().forEach(category->{
			list.add(category.toDto());
		});
		
		return list;
	}
	
	//좋아요(추천) 기능
	public int getLikeCount(int bookId) {
		return bookRepository.getLikeCount(bookId);
	}
	// 좋아요 상태
	public int getLikeStatus(int bookId, int userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("bookId", bookId);
		map.put("userId", userId);
		
		return bookRepository.getLikeStatus(map);
	}
	// 좋아요 등록
	public int setLike(int bookId, int userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("bookId", bookId);
		map.put("userId", userId);
		
		return bookRepository.setLike(map);
	}
	// 좋아요 최소
	public int disLike(int bookId, int userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("bookId", bookId);
		map.put("userId", userId);
		
		return bookRepository.disLike(map);
	}
	
	// 렌탈 기능
	public List<RentalListRespDto> getRentalListByBookId(int bookId){
		List<RentalListRespDto> list = new ArrayList<>();
		bookRepository.getRentalListByBookId(bookId).forEach(rentalData -> {
			list.add(rentalData.toDto());
		});
		
		return list;
	}
	//대여 서비스
	public int rentalBook(int bookListId, int userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("bookListId", bookListId);
		map.put("userId", userId);
		
		return bookRepository.rentalBook(map);
	}
	//반납 서비스
	public int returnBook(int bookListId, int userId) {
		Map<String, Object> map = new HashMap<>();
		map.put("bookListId", bookListId);
		map.put("userId", userId);
		
		return bookRepository.returnBook(map);
	}
	
}

package todolistservice.dtos;



import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PageResult<T> {

	private int page;
	private int size;
	private long totalResults;
	private int totalPages;
	
	@JsonIgnore
	private List<T> results = new ArrayList<>(); 
	
	@JsonIgnore
	public int getNextPage() {
		return  isLastPage() ? -1 : (page + 1);
	}
	
	@JsonIgnore
	public int getPreviousPage() {
		return  isFirstPage() ? -1 : (page - 1);
	}
	
	@JsonIgnore
	private boolean isLastPage() {
		return (page + 1) == totalPages;
	}
	
	@JsonIgnore
	public boolean isFirstPage() {
		return this.page == 0;
	}
}



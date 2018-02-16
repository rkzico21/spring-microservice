package todolistservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import todolistservice.entities.TodoListItem;

@RepositoryRestResource(exported=false)
@Repository
public interface TodoListRepository  extends PagingAndSortingRepository<TodoListItem, Long>, JpaSpecificationExecutor<TodoListItem>   {

	Page<TodoListItem> findByUserId(@Param("userid")Long userId, Pageable pageRequest);
}

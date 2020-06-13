package com.banking.app.MyBankingApp.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.banking.app.MyBankingApp.domain.User;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {

	//make sure the coder implements these two methods
	User findByUsername(String username);
	User getById(Long id);

}

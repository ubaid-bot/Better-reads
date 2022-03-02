package com.betterreads.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.betterreads.model.UserBooks;
import com.betterreads.model.UserBooksPrimaryKey;

public interface UserBooksRepository extends CassandraRepository<UserBooks, UserBooksPrimaryKey> {
    
}
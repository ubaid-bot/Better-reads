package com.betterreads.dataloader.repository;

import com.betterreads.dataloader.model.Book;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CassandraRepository<Book,String> {

}

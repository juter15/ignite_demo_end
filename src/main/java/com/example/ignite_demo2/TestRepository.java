package com.example.ignite_demo2;

import org.apache.ignite.springdata20.repository.IgniteRepository;
import org.apache.ignite.springdata20.repository.config.RepositoryConfig;

@RepositoryConfig(cacheName = "test1")
public interface TestRepository extends IgniteRepository<test1, Long> {
}

package com.maybe.work.repository;

import com.maybe.work.model.Furlough;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FurloughRepository extends JpaRepository<Furlough, String> {
}

package com.oberasoftware.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * @author renarj
 */
public interface DataEntryRepository extends CrudRepository<DataEntry, Long> {
    Page<DataEntry> findAll(Pageable pageable);

    Page<DataEntry> findByYearAndMonth(Pageable pageable, int year, int month);
}

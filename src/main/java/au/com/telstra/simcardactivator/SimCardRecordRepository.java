package au.com.telstra.simcardactivator;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SimCardRecordRepository extends JpaRepository<SimCardRecord, Long> {
    // No implementation needed. Spring creates the methods for you:
    // .save(), .findById(), .findAll(), etc.
}

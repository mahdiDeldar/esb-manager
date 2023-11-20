package org.sohagroup.repository;

import org.sohagroup.domain.Flow;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlowRepository extends JpaRepository<Flow,Long> {
  Optional<Flow> findFlowByEndPoint(String endPoint);
}

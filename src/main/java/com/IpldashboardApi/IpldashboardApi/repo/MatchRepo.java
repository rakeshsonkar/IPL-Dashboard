package com.IpldashboardApi.IpldashboardApi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.IpldashboardApi.IpldashboardApi.entities.Match;
@Repository
public interface MatchRepo extends JpaRepository<Match,Long>{

}

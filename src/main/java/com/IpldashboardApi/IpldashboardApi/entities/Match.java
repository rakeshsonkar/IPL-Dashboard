package com.IpldashboardApi.IpldashboardApi.entities;



import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "IplMatch")
public class Match {
	@Id
	private Long id;	
	private String city;	
	private Date date;
	private String season;
	private String matchNumber;
	private String team1;
	private String team2;
	private String venue;
	private String tossWinner;
	private String tossDecision;
	private String superOver;
	private String winningTeam;
	private String WonBy;
	private String Margin;
	private String checkMethod;
	private String playerOfMatch;
	private String team1Players;
	private String team2Players;
	private String umpire1;
	private String umpire2;

}

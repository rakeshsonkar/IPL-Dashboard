package com.IpldashboardApi.IpldashboardApi.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.springframework.batch.item.ItemProcessor;

import com.IpldashboardApi.IpldashboardApi.entities.Match;
import com.IpldashboardApi.IpldashboardApi.request.InputDataSet;


public class MatchDataProcess implements ItemProcessor<InputDataSet, Match> {
	


	@Override
	public Match process(InputDataSet item) throws Exception {
		Match obj= new Match();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		obj.setId(Long.parseLong(item.getID()));
		obj.setCity(item.getCity());
		obj.setDate(dateFormat.parse(item.getDate()));
		obj.setSeason(item.getSeason());
		obj.setMatchNumber(item.getMatchNumber());
		
		obj.setVenue(item.getVenue());
		String fristInningsTeam, secondInningsTeam;
	if("bat".equals(item.getTossDecision())) {
		fristInningsTeam=item.getTossWinner();
		secondInningsTeam= item.getTossWinner().equals(item.getTeam1()) ? item.getTeam2():item.getTeam1();
		
	}else{
		 secondInningsTeam = item.getTossWinner();
		 fristInningsTeam = item.getTossWinner().equals(item.getTeam1()) ? item.getTeam2():item.getTeam1();
	}
		obj.setTeam1(fristInningsTeam);
		obj.setTeam2(secondInningsTeam);
		obj.setTeam1Players(item.getTeam1Players());
		obj.setTeam2Players(item.getTeam1Players());
		obj.setTossWinner(item.getTossWinner());
		obj.setTossDecision(item.getTossDecision());
		obj.setSuperOver(item.getSuperOver());
		obj.setWinningTeam(item.getWinningTeam());
		obj.setWonBy(item.getWonBy());
		obj.setMargin(item.getMargin());
		obj.setCheckMethod(item.getMethod());
		obj.setPlayerOfMatch(item.getPlayer_of_Match());
		obj.setUmpire1(item.getUmpire1());
		obj.setUmpire2(item.getUmpire2());
		return obj;
	}
	
	

}

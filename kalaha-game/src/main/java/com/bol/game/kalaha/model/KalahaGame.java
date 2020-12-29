package com.bol.game.kalaha.model;

import static com.bol.game.kalaha.model.PlayetPitsSequence.fifthPitPlayerOne;
import static com.bol.game.kalaha.model.PlayetPitsSequence.fifthPitPlayerTwo;
import static com.bol.game.kalaha.model.PlayetPitsSequence.firstPitPlayerOne;
import static com.bol.game.kalaha.model.PlayetPitsSequence.firstPitPlayerTwo;
import static com.bol.game.kalaha.model.PlayetPitsSequence.fourthPitPlayerOne;
import static com.bol.game.kalaha.model.PlayetPitsSequence.fourthPitPlayerTwo;
import static com.bol.game.kalaha.model.PlayetPitsSequence.housePlayerOne;
import static com.bol.game.kalaha.model.PlayetPitsSequence.housePlayerTwo;
import static com.bol.game.kalaha.model.PlayetPitsSequence.secondPitPlayerOne;
import static com.bol.game.kalaha.model.PlayetPitsSequence.secondPitPlayerTwo;
import static com.bol.game.kalaha.model.PlayetPitsSequence.sixthPitPlayerOne;
import static com.bol.game.kalaha.model.PlayetPitsSequence.sixthPitPlayerTwo;
import static com.bol.game.kalaha.model.PlayetPitsSequence.thirdPitPlayerOne;
import static com.bol.game.kalaha.model.PlayetPitsSequence.thirdPitPlayerTwo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import com.bol.game.kalaha.exception.KalahaGameException;
import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Table("gamer")
public class KalahaGame implements Serializable {

	private static final long serialVersionUID = 5113298685412470208L;

	@PrimaryKey
	private String id = Uuids.timeBased().toString();

	@Column(value = "player_pits")
	private List<KalahaPlayerPit> playerPits;

	@Column(value = "player_turn")
	private KalahaPlayerList playerTurn;

	@JsonIgnore
	private int currentPitId;

	public KalahaGame() {
		this(6);
	}

	public KalahaGame(int pitStones) {
		this.playerPits = Arrays.asList(new KalahaPlayerPit(firstPitPlayerOne, pitStones),
				new KalahaPlayerPit(secondPitPlayerOne, pitStones), new KalahaPlayerPit(thirdPitPlayerOne, pitStones),
				new KalahaPlayerPit(fourthPitPlayerOne, pitStones), new KalahaPlayerPit(fifthPitPlayerOne, pitStones),
				new KalahaPlayerPit(sixthPitPlayerOne, pitStones), new KalahaPlayerHouse(housePlayerOne),
				new KalahaPlayerPit(firstPitPlayerTwo, pitStones), new KalahaPlayerPit(secondPitPlayerTwo, pitStones),
				new KalahaPlayerPit(thirdPitPlayerTwo, pitStones), new KalahaPlayerPit(fourthPitPlayerTwo, pitStones),
				new KalahaPlayerPit(fifthPitPlayerTwo, pitStones), new KalahaPlayerPit(sixthPitPlayerTwo, pitStones),
				new KalahaPlayerHouse(housePlayerTwo));
	}

	public KalahaGame(String id, int pitStones) {
		this(pitStones);
		this.id = id;
	}
	
	public KalahaGame(String id, int pitStones, int houseOneStones, int houseTwoStones) {
		this.playerPits = Arrays.asList(new KalahaPlayerPit(firstPitPlayerOne, pitStones),
				new KalahaPlayerPit(secondPitPlayerOne, pitStones), new KalahaPlayerPit(thirdPitPlayerOne, pitStones),
				new KalahaPlayerPit(fourthPitPlayerOne, pitStones), new KalahaPlayerPit(fifthPitPlayerOne, pitStones),
				new KalahaPlayerPit(sixthPitPlayerOne, pitStones), new KalahaPlayerHouse(housePlayerOne, houseOneStones),
				new KalahaPlayerPit(firstPitPlayerTwo, pitStones), new KalahaPlayerPit(secondPitPlayerTwo, pitStones),
				new KalahaPlayerPit(thirdPitPlayerTwo, pitStones), new KalahaPlayerPit(fourthPitPlayerTwo, pitStones),
				new KalahaPlayerPit(fifthPitPlayerTwo, pitStones), new KalahaPlayerPit(sixthPitPlayerTwo, pitStones),
				new KalahaPlayerHouse(housePlayerTwo, houseTwoStones));
	}
	
	 public KalahaPlayerPit getCurrentPitId(Integer pitIndex) throws KalahaGameException {
	        try {
	            return this.playerPits.get(pitIndex-1);
	        }catch (Exception e){
	            throw  new KalahaGameException("Invalid pitIndex: "+ pitIndex);
	        }
	    }
	 
	 public KalahaPlayerPit getScore(Integer pitIndex) throws KalahaGameException {
	        try {
	            return this.playerPits.get(pitIndex-1);
	        }catch (Exception e){
	            throw  new KalahaGameException("Invalid pitIndex: "+ pitIndex);
	        }
	    }
}

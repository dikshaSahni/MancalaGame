package com.bol.game.kalaha.service;

import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.bol.game.kalaha.exception.GameNotFoundException;
import com.bol.game.kalaha.model.KalahaGame;
import com.bol.game.kalaha.model.KalahaPlayerList;
import com.bol.game.kalaha.model.KalahaPlayerPit;
import com.bol.game.kalaha.model.PlayetPitsSequence;
import com.bol.game.kalaha.respository.KalahaGameRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KalahaGameService {

	@Value("${kalaha.total.pits}")
	private int totalPits = 14;

	@Autowired
	private KalahaGameRepository kalahaGameRepository;

	public KalahaGame createGame(int pitStones) {
		KalahaGame kalahaGame = new KalahaGame(pitStones);
		log.info("Game Id: " + kalahaGame.getId());
		kalahaGameRepository.save(kalahaGame);
		return kalahaGame;
	}

	public KalahaGame playGame(KalahaGame kalahaGame, Integer pitId) {
		
		//has Game Ended Validation
		hasGameEnded(kalahaGame);

		// not start with the house of any player
		if (pitId == PlayetPitsSequence.housePlayerOne || pitId == PlayetPitsSequence.housePlayerTwo)
			return kalahaGame;

		// identify turn
		if (kalahaGame.getPlayerTurn() == null) {
			if (pitId < PlayetPitsSequence.housePlayerOne)
				kalahaGame.setPlayerTurn(KalahaPlayerList.PlayerOne);
			else
				kalahaGame.setPlayerTurn(KalahaPlayerList.PlayerTwo);
		}

		// always right player
		if (kalahaGame.getPlayerTurn() == KalahaPlayerList.PlayerOne && pitId > PlayetPitsSequence.housePlayerOne
				|| kalahaGame.getPlayerTurn() == KalahaPlayerList.PlayerTwo
						&& pitId < PlayetPitsSequence.housePlayerTwo)
			return kalahaGame;

		// check stones in current pit; if empty, no move
		KalahaPlayerPit currentPit = kalahaGame.getCurrentPitId(pitId);
		int stones = currentPit.getNumberOfStones();
		if (stones == 0)
			return kalahaGame;

		// take all stones from current pit
		currentPit.setNumberOfStones(0);

		// start putting one stone in each next pit to the right until last stone
		kalahaGame.setCurrentPitId(pitId);
		IntStream.range(0, stones - 1).forEach(i -> moveRight(kalahaGame, false));

		// move last stone; true to check where it lands
		moveRight(kalahaGame, true);

		// if last stone lands in big pit, do not change the turn, else change the turn
		int currentPidId = kalahaGame.getCurrentPitId();
		if (currentPidId != PlayetPitsSequence.housePlayerOne && currentPidId != PlayetPitsSequence.housePlayerTwo)
			kalahaGame.setPlayerTurn(nextTurn(kalahaGame.getPlayerTurn()));

		return kalahaGame;
	}

	private void moveRight(KalahaGame game, boolean lastStone) {
		int currentPitIndex = game.getCurrentPitId() % totalPits + 1;
		log.debug("Current pit: " + currentPitIndex);
		KalahaPlayerList playerTurn = game.getPlayerTurn();

		// player one cannot put stones in player two's house and vice versa
		if ((currentPitIndex == PlayetPitsSequence.housePlayerOne && playerTurn == KalahaPlayerList.PlayerTwo)
				|| (currentPitIndex == PlayetPitsSequence.housePlayerTwo && playerTurn == KalahaPlayerList.PlayerOne))
			currentPitIndex = currentPitIndex % totalPits + 1;

		game.setCurrentPitId(currentPitIndex);

		KalahaPlayerPit nextPit = game.getCurrentPitId(currentPitIndex);
		if (!lastStone || currentPitIndex == PlayetPitsSequence.housePlayerOne
				|| currentPitIndex == PlayetPitsSequence.housePlayerTwo) {
			nextPit.dropOneStone();
			return;
		}

		KalahaPlayerPit oppositePit = game.getCurrentPitId(totalPits - currentPitIndex);

		// check the no. of stones in opposite pit
		if (nextPit.isEmpty() && !oppositePit.isEmpty()) {
			int oppositeStones = oppositePit.getNumberOfStones();
			oppositePit.takeAllStones();
			int pitHouseIndex = currentPitIndex < PlayetPitsSequence.housePlayerOne ? PlayetPitsSequence.housePlayerOne
					: PlayetPitsSequence.housePlayerOne;
			KalahaPlayerPit pitHouse = game.getCurrentPitId(pitHouseIndex);
			pitHouse.addStones(oppositeStones + 1);
			return;
		}

		nextPit.dropOneStone();
	}

	private KalahaPlayerList nextTurn(KalahaPlayerList currentTurn) {
		if (currentTurn == KalahaPlayerList.PlayerOne)
			return KalahaPlayerList.PlayerTwo;
		return KalahaPlayerList.PlayerOne;
	}

	public KalahaPlayerList hasGameEnded(KalahaGame game) {

		int houseOneStones = game.getCurrentPitId(PlayetPitsSequence.housePlayerOne).getNumberOfStones();
		int houseTwoStones = game.getCurrentPitId(PlayetPitsSequence.housePlayerTwo).getNumberOfStones();

		if (houseOneStones + houseTwoStones == 72) {
			log.warn("GAME has ended!!");

			if (houseOneStones > houseTwoStones) {
				log.info("Winner is Player One");
				return KalahaPlayerList.PlayerOne;
			} else {
				log.info("Winner is Player Two");
				return KalahaPlayerList.PlayerOne;
			}
		}
		return null;
	}

	@Cacheable(value = "kalahaGame", key = "#id", unless = "#result  == null")
	public KalahaGame loadGame(String id) throws GameNotFoundException {
		Optional<KalahaGame> gameStatus = kalahaGameRepository.findById(id);

		if (!gameStatus.isPresent()) {
			log.error("Game id " + id + " not found!");
			throw new GameNotFoundException("Game id " + id + " not found!");
		}
		return gameStatus.get();
	}

	// updating Cache
	@CachePut(value = "kalahaGame", key = "#kalahaGame.id")
	public KalahaGame updateCacheAndGame(KalahaGame kalahaGame) {
		kalahaGame = kalahaGameRepository.save(kalahaGame);
		return kalahaGame;
	}
}

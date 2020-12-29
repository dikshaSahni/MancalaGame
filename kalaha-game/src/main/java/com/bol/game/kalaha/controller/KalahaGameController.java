package com.bol.game.kalaha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bol.game.kalaha.exception.KalahaGameException;
import com.bol.game.kalaha.model.KalahaGame;
import com.bol.game.kalaha.model.PlayetPitsSequence;
import com.bol.game.kalaha.service.KalahaGameService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/game")
@CrossOrigin
public class KalahaGameController {

	@Autowired
	private KalahaGameService kalahaGameService;

	@Value("${kalaha.pit.stones:6}")
	private int pitStones;

	@PostMapping(path = "/create", produces = "application/json")
	public ResponseEntity<KalahaGame> createGame() throws Exception {
		KalahaGame kalahaGame = kalahaGameService.createGame(pitStones);
		
		kalahaGameService.updateCacheAndGame(kalahaGame);
		return new ResponseEntity<>(kalahaGame, HttpStatus.CREATED);
	}

	@GetMapping("/status/{gameId}")
	public ResponseEntity<KalahaGame> loadGameStatus( @PathVariable(name = "gameId", required = true) String gameId) throws Exception {
		return new ResponseEntity<>(kalahaGameService.loadGame(gameId), HttpStatus.OK);
	}

	@PutMapping(value = "/play/{gameId}/pit/{pitId}")
	public ResponseEntity<KalahaGame> playGame(@PathVariable(name = "gameId", required = true) String gameId,
			@PathVariable(name = "pitId", required = true) Integer pitId) throws Exception {

		if (pitId == null || pitId < 1 || pitId >= PlayetPitsSequence.housePlayerOne
				|| pitId == PlayetPitsSequence.housePlayerTwo) {
			log.error("Invalid pit Index");
			throw new KalahaGameException("The pit index is not valid.");
		}

		KalahaGame kalahaGame = kalahaGameService.loadGame(gameId);
		kalahaGame = kalahaGameService.playGame(kalahaGame, pitId);

		kalahaGameService.updateCacheAndGame(kalahaGame);

		return new ResponseEntity<>(kalahaGame, HttpStatus.OK);
	}

}

package com.bol.game.kalaha.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.bol.game.kalaha.exception.GameNotFoundException;
import com.bol.game.kalaha.model.KalahaGame;
import com.bol.game.kalaha.model.KalahaPlayerList;
import com.bol.game.kalaha.respository.KalahaGameRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class KalahaGameServiceTest {

	private KalahaGame game;
	
	@Mock
    private KalahaGameRepository mockRepository;

    @Autowired
    @InjectMocks
    private KalahaGameService kalahaGameService;
    
	ResourceLoader resourceLoader = new DefaultResourceLoader();
	Resource playGameJson = resourceLoader.getResource("classpath:play-game.json");
    
	@Before
	public void setUp() throws Exception {
		this.game = new KalahaGame(6);
	}

	@Test(expected= GameNotFoundException.class)
	public void testGameStatus_NotFound() {
		when(mockRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
		kalahaGameService.loadGame("da43cca0-43da-11eb-8f7d-fb8ab5b8d5c9");
	}
	
	@Test
	public void testPlayGame_PlayerOnePitTwo() {
		KalahaGame resultGame = kalahaGameService.playGame(game, 2);
		assertThat(resultGame.getPlayerTurn().compareTo(KalahaPlayerList.PlayerTwo));
	}
	
	@Test
	public void testPlayGame_RepeatChance() {
		KalahaGame resultGame = kalahaGameService.playGame(game, 1);
		assertThat(resultGame.getPlayerTurn().compareTo(KalahaPlayerList.PlayerOne));
	}

	@Test
	public void testPlayGame_StartingWithLeftPlayer_NotAllowed() {
		KalahaGame resultGame = kalahaGameService.playGame(game, 8);
		assertThat(resultGame.getPlayerTurn().compareTo(KalahaPlayerList.PlayerTwo));
	}
	
	@Test
	public void testPlayGame() {
		KalahaGame resultGame = kalahaGameService.playGame(game, 1);
		
		if(resultGame.getPlayerTurn().compareTo(KalahaPlayerList.PlayerOne) == 0) {
			resultGame = kalahaGameService.playGame(resultGame, 4);
		}
		assertThat(resultGame.getPlayerPits().toString()).isEqualTo("[1:0, 2:7, 3:7, 4:0, 5:8, 6:8, 7:2, 8:7, 9:7, 10:7, 11:7, 12:6, 13:6, 14:0]");
		assertThat(resultGame.getPlayerTurn().compareTo(KalahaPlayerList.PlayerTwo));
	}
	
	@Test
	public void testPlayGame_playingWithOppositeStones() {
		KalahaGame resultGame = kalahaGameService.playGame(game, 2);
		resultGame = kalahaGameService.playGame(resultGame, 4);
		resultGame = kalahaGameService.playGame(resultGame, 11);
		resultGame = kalahaGameService.playGame(resultGame, 4);
		resultGame = kalahaGameService.playGame(resultGame, 8);
		resultGame = kalahaGameService.playGame(resultGame, 1);
		resultGame = kalahaGameService.playGame(resultGame, 11);
		resultGame = kalahaGameService.playGame(resultGame, 6);
		
		assertThat(resultGame.getPlayerPits().toString()).isEqualTo("[1:6, 2:0, 3:7, 4:7, 5:7, 6:7, 7:1, 8:7, 9:6, 10:6, 11:6, 12:6, 13:6, 14:0]");
		assertThat(resultGame.getPlayerTurn().compareTo(KalahaPlayerList.PlayerOne));
	}
	
	@Test
	public void hasGameEnded() {
		KalahaGame kalahaGame = new KalahaGame("da43cca0-43da-11eb-8f7d-fb8ab5b8d5c9", 0, 40, 32);
		KalahaPlayerList player = kalahaGameService.hasGameEnded(kalahaGame);
		assertThat(player.compareTo(KalahaPlayerList.PlayerOne));
	}
}

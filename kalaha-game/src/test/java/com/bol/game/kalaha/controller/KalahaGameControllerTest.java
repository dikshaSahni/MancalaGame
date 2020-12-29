package com.bol.game.kalaha.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import com.bol.game.kalaha.exception.GameNotFoundException;
import com.bol.game.kalaha.model.KalahaGame;
import com.bol.game.kalaha.service.KalahaGameService;

@WebMvcTest(value = KalahaGameController.class)
@RunWith(SpringRunner.class)
public class KalahaGameControllerTest {

	@MockBean
	KalahaGameService mockKalahaGameService;

	@Autowired
	private MockMvc mockMvc;
	
	ResourceLoader resourceLoader = new DefaultResourceLoader();
	Resource newGameJson = resourceLoader.getResource("classpath:new-game.json");
	Resource playGameJson = resourceLoader.getResource("classpath:play-game.json");

	@Test
	public void testCreateGame() throws IOException, Exception {
		KalahaGame expectedGameValue = new KalahaGame("da43cca0-43da-11eb-8f7d-fb8ab5b8d5c9", 6);
		when(mockKalahaGameService.createGame(6)).thenReturn(expectedGameValue);

		this.mockMvc.perform(post("/game/create")).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(StreamUtils.copyToString(newGameJson.getInputStream(), Charset.defaultCharset())))
						.andReturn();
	}
	
	@Test
	public void testLoadGameStatus() throws IOException, Exception {
		KalahaGame expectedGameValue = new KalahaGame("da43cca0-43da-11eb-8f7d-fb8ab5b8d5c9", 6);
		when(mockKalahaGameService.loadGame("da43cca0-43da-11eb-8f7d-fb8ab5b8d5c9")).thenReturn(expectedGameValue);

		this.mockMvc.perform(get("/game/status/da43cca0-43da-11eb-8f7d-fb8ab5b8d5c9")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(StreamUtils.copyToString(newGameJson.getInputStream(), Charset.defaultCharset())))
						.andReturn();
	}
	
	@Test
	public void testLoadGameStatus_gameNotFound() throws IOException, Exception {
		when(mockKalahaGameService.loadGame("da43cca0-43da-11eb-8f7d")).thenThrow(GameNotFoundException.class);

		this.mockMvc.perform(get("/game/status/da43cca0-43da-11eb-8f7d")).andExpect(status().isNotFound());
	}

	@Test
	public void testPlayGame_InvalidId() throws IOException, Exception {
		KalahaGame expectedGameValue = new KalahaGame("da43cca0-43da-11eb-8f7d-fb8ab5b8d5c9", 6);
		when(mockKalahaGameService.loadGame("da43cca0-43da-11eb-8f7d-fb8ab5b8d5c9")).thenReturn(expectedGameValue);

		this.mockMvc.perform(put("/game/play/da43cca0-43da-11eb-8f7d-fb8ab5b8d5c9/pit/7"))
                .andExpect(status().isInternalServerError())
                .andReturn();
	}
}
